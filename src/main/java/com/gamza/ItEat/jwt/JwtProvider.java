package com.gamza.ItEat.jwt;

import com.gamza.ItEat.enums.UserRole;
import com.gamza.ItEat.error.ErrorCode;
import com.gamza.ItEat.error.exeption.ForbiddenException;
import com.gamza.ItEat.repository.UserRepository;
import com.gamza.ItEat.service.jwt.CustomUserDetailService;
import com.gamza.ItEat.service.jwt.RedisService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class JwtProvider {

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final CustomUserDetailService customUserDetailService;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.accessTokenExpiration}")
    private long accessTokenValidTime;

    @Value("${spring.jwt.refreshTokenExpiration}")
    private long refreshTokenValidTime;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(String userEmail, UserRole userRole) {
        return this.createToken(userEmail, userRole, accessTokenValidTime, "access");
    }

    public String createRefreshToken(String userEmail, UserRole userRole) {
        return this.createToken(userEmail, userRole, refreshTokenValidTime, "refresh");
    }

    public String createToken(String userEmail, UserRole userRole, long tokenValid, String tokenType) {
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("roles", userRole.toString());
        claims.put("type", tokenType);

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Date date = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + tokenValid))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(this.getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserEmail(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build();

        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }


    public String reissueAccessToken(String refreshToken) {
        String email = redisService.getValues(refreshToken).get("email");
        if (Objects.isNull(email)) {
            throw new ForbiddenException("401", ErrorCode.ACCESS_DENIED_EXCEPTION);
        }

        return createAccessToken(email, userRepository.findByEmail(email).get().getUserRole());
    }

    public String reissueRefreshToken(String refreshToken) {
        String email = redisService.getValues(refreshToken).get("email");
        if (Objects.isNull(email)) {
            throw new ForbiddenException("401", ErrorCode.ACCESS_DENIED_EXCEPTION);
        }

        String newRefreshToken = createRefreshToken(email, userRepository.findByEmail(email).get().getUserRole());

        redisService.delValues(refreshToken);
        redisService.setValues(newRefreshToken, email);

        return newRefreshToken;
    }

    public String resolveAccessToken(HttpServletRequest request) {
        if (request.getHeader("authorization") != null)
            return request.getHeader("authorization").substring(7);
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        if (request.getHeader("refreshToken") != null)
            return request.getHeader("refreshToken").substring(7);
        return null;
    }

    public void expireToken(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Date expiration = claims.getExpiration();
        Date now = new Date();
        if (now.after(expiration)) {
            redisService.addTokenToBlacklist(token, expiration.getTime() - now.getTime());
        }
    }

    public boolean validateToken(String jwtToken) {
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, "Token has expired");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("JWT token is unsupported");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT claims string is empty");
        }
    }

    //토큰 헤더 설정
    public void setHeaderAT(HttpServletResponse response, String accessToken) {
        response.setHeader("authorization", "bearer "+ accessToken);
    }
    public void setHeaderRT(HttpServletResponse response, String refreshToken) {
        response.setHeader("refreshToken", "bearer "+ refreshToken);
    }

    public String extractTokenType(String token) {
        Claims claims = extractClaims(token);
        if(claims != null && claims.containsKey("type")) {
            return (String) claims.get("type");
        } else {
            throw new UnsupportedJwtException("JWT 토큰이 타입이 없습니다.");
        }
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (IllegalArgumentException e) {
            throw new UnsupportedJwtException("토큰 타입을 추출할 수 없습니다.");
        }
    }

}