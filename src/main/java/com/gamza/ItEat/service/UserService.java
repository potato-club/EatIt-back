package com.gamza.ItEat.service;


import com.gamza.ItEat.dto.user.LoginRequestDto;
import com.gamza.ItEat.dto.user.LoginResponseDto;
import com.gamza.ItEat.dto.user.SignUpRequestDto;
import com.gamza.ItEat.entity.UserEntity;
import com.gamza.ItEat.enums.UserRole;
import com.gamza.ItEat.error.ErrorCode;
import com.gamza.ItEat.error.exeption.UnAuthorizedException;
import com.gamza.ItEat.jwt.JwtProvider;
import com.gamza.ItEat.repository.UserRepository;
import com.gamza.ItEat.service.jwt.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.gamza.ItEat.error.ErrorCode.ACCESS_DENIED_EXCEPTION;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto requestDto, HttpServletResponse response) {
        UserEntity userEntity = userRepository.findByEmail(requestDto.getEmail()).orElseThrow();

        //패스워드 다를 때
        if (!passwordEncoder.matches(requestDto.getPassword(), userEntity.getPassword())) {
            throw new UnAuthorizedException("401", ACCESS_DENIED_EXCEPTION);
        }

        this.setJwtTokenInHeader(requestDto.getEmail(), response);

        return LoginResponseDto.builder()
                .responseCode("200")
                .build();
    }
    public void signUp(SignUpRequestDto requestDto, HttpServletResponse response) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new UnAuthorizedException("401", ACCESS_DENIED_EXCEPTION);
        }
        //카카오 로그인 로직 추후 추가
        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        UserEntity userEntity = requestDto.toEntity();
        userRepository.save(userEntity);
    }

    public void logout(HttpServletRequest request) {
        redisService.delValues(jwtProvider.resolveRefreshToken(request));
        jwtProvider.expireToken(jwtProvider.resolveAccessToken(request));
    }


    public void setJwtTokenInHeader(String email, HttpServletResponse response) {
        UserRole userRole = userRepository.findByEmail(email).get().getUserRole();

        String accessToken = jwtProvider.createAccessToken(email, userRole);
        String refreshToken = jwtProvider.createRefreshToken(email, userRole);


        jwtProvider.setHeaderAT(response, accessToken);
        jwtProvider.setHeaderRT(response, refreshToken);

        redisService.setValues(refreshToken, email);
    }

    public Optional<UserEntity> findByUserToken(HttpServletRequest request) {
        String token = jwtProvider.resolveAccessToken(request);
        String accessTokenType = jwtProvider.extractTokenType(token);

        if("refresh".equals(accessTokenType)) {
            throw new UnAuthorizedException("RefreshToken은 사용할 수 없습니다.", ErrorCode.INVALID_TOKEN_EXCEPTION);
        }

        return token == null ? null : userRepository.findByEmail(jwtProvider.getUserEmail(token));
    }

}

