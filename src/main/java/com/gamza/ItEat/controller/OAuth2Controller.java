//package com.gamza.ItEat.controller;
//
//import com.gamza.ItEat.service.OAuth2Service;
//import io.swagger.v3.oas.annotations.Operation;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/kakao")
//@RequiredArgsConstructor
//public class OAuth2Controller {
//    private OAuth2Service oAuth2Service;
//
//
//    @Operation(summary = "OAuth 로그인, HttpStatus = 기가입자 200, 신규 201")
//    @PostMapping("/oauth")
//    public ResponseEntity<Void> getToken(@RequestBody OAuthRequest oAuthRequest, HttpServletResponse response) {
//        return oAuth2Service.oAuthLogin(oAuthRequest, response);
//    }
//    @Operation(summary = "OAuth 회원가입 정보추가, AccessToken 헤더 필수")
//    @PostMapping("/oauth/register")
//    public void oauth2Register(@RequestBody OAuthRegisterRequest oAuthRegisterRequest, HttpServletRequest request) {
//        oAuth2Service.oauth2Register(oAuthRegisterRequest, request);
//    }
//}
