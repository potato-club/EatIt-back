//package com.gamza.ItEat.service;
//
//
//import com.gamza.ItEat.entity.UserEntity;
//import com.gamza.ItEat.error.exeption.UnAuthorizedException;
//import com.gamza.ItEat.jwt.JwtProvider;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.apache.coyote.BadRequestException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.net.URLDecoder;
//import java.nio.charset.StandardCharsets;
//import java.security.Provider;
//import java.time.LocalDate;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//
//@Service
//@RequiredArgsConstructor
//public class OAuth2Service {
//    private final UserRepo userRepo;
//    private final JwtProvider jwtTokenProvider;
//    private final RestTemplate restTemplate;
//
//
//    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
//    private String kakaoClientId;
//    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
//    private String kakaoClientSecret;
//    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
//    private String kakaoRedirectUri;
//
//    @Transactional
//    public void oauth2Register(OAuthRegisterRequest oAuthRegisterRequest, HttpServletRequest request) {
//        String AT = jwtTokenProvider.resolveAccessToken(request);
//        if (AT == null)
//            throw new UnAuthorizedException("토큰이 필요한 요청입니다.", ErrorCode.UNAUTHORIZED_EXCEPTION);
//        String userId = jwtTokenProvider.getUserId(AT);
//        UserEntity userEntity = userRepo.findByUserId(userId).orElseThrow(()->new UnAuthorizedException("토큰 오류", ErrorCode.UNAUTHORIZED_EXCEPTION));
//        userEntity.oAuthRegisterUpdate(oAuthRegisterRequest);
//        Tos tos = userEntity.getTos();
//        tos.oAuthRegisterUpdate(oAuthRegisterRequest.getTosInfoList());
//    }
//    @Transactional
//    public ResponseEntity<Void> oAuthLogin(OAuthRequest oAuthRequest, HttpServletResponse response) {
//        String providerResponseToken = this.getToken(oAuthRequest);
//        Map<String, Object> clientAccount = this.getUserInfo(oAuthRequest.getProvider(), providerResponseToken);
//        String userid, nickName;
//        if (oAuthRequest.getProvider().equals(Provider.kakao)) {
//            Map<String, Object> profile = (Map) clientAccount.get("profile");
//            userid = (String) clientAccount.get("email");
//            nickName =(String) profile.get("nickname");
//        } else {
//            userid = (String) clientAccount.get("email");
//            nickName = (String) clientAccount.get("given_name");
//        }
//
//
//        AtomicBoolean isNew = new AtomicBoolean(false);
//        UserEntity userEntity = userRepo.findByUserId(userid).orElseGet(()->{
//            isNew.set(true);
//            return UserEntity.builder()
//                    .provider(oAuthRequest.getProvider())
//                    .userName("익명")
//                    .nickName(nickName)
//                    .birthDay(LocalDate.now())
//                    .userRole(UserRole.Normal)
//                    .userId(userid)
//                    .agreementToUseSMS(false)
//                    .build();
//        });
//        if (isNew.get()) {
//            if (userRepo.existsByUserId(userid))
//                throw new DuplicateException("이미 사용되고 있는 email 입니다.", ErrorCode.DUPLICATE_EXCEPTION);
//        }
//
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userid, null);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String RT = jwtTokenProvider.createRefreshToken(userEntity.getUserId(), userEntity.getUserRole(), userEntity.getUserImg());
//        String AT = jwtTokenProvider.createAccessToken(userEntity.getUserId(), userEntity.getUserRole(), userEntity.getUserImg());
//
//        userEntity.updateRefreshToken(RT);
//
//        jwtTokenProvider.setHeaderAccessToken(response, AT);
//        jwtTokenProvider.setHeaderRefreshToken(response, RT);
//        if(isNew.get()) {
//            tosJpaRepo.save(userEntity.getTos());
//            userRepo.save(userEntity);
//            return ResponseEntity.status(201).build();
//        }
//        return ResponseEntity.ok().build();
//    }
//
//    private String getToken(OAuthRequest oAuthRequest) {
//
//        String tokenEndpoint, clientId, clientSecret, redirectUri;
//
//        switch (oAuthRequest.getProvider()) {
//            case kakao -> {
//                tokenEndpoint = "https://kauth.kakao.com/oauth/token";
//                clientId = kakaoClientId;
//                clientSecret = kakaoClientSecret;
//                redirectUri = kakaoRedirectUri;
//            }
//
//            default -> throw new BadRequestException("cannot matched provider", ErrorCode.BAD_REQUEST_EXCEPTION);
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("code", oAuthRequest.getCode());
//        params.add("client_id", clientId);
//        params.add("client_secret", clientSecret);
//        params.add("redirect_uri", redirectUri);
//        params.add("grant_type", "authorization_code");
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//        ResponseEntity<Map> response = restTemplate.exchange(tokenEndpoint, HttpMethod.POST, request, Map.class);
//
//        return (String) response.getBody().get("access_token");
//    }
//    private Map<String, Object> getUserInfo(Provider provider, String accessToken) {
//        String userInfoEndpoint;
//
//        userInfoEndpoint = "https://kapi.kakao.com/v2/user/me";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//
//        HttpEntity<String> request = new HttpEntity<>(headers);
//        ResponseEntity<Map> response = restTemplate.exchange(userInfoEndpoint, HttpMethod.GET, request, Map.class);
//
//        return (Map<String, Object>) response.getBody().get("kakao_account");
//        }
//    }
//
//
//}
