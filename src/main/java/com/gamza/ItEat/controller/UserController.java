package com.gamza.ItEat.controller;

import com.gamza.ItEat.dto.user.*;
import com.gamza.ItEat.service.UserService;
import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "User API")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public LoginResponseDto login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        return userService.login(requestDto, response);
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<String> userSignUp(@RequestBody SignUpRequestDto requestDto, HttpServletResponse response) {
        userService.signUp(requestDto, response);
        return ResponseEntity.ok("회원가입 완료!");
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        userService.logout(request);
        return ResponseEntity.ok("로그아웃 완료!");
    }

    @PostMapping("/update")
    @Operation(summary = "유저정보 수정")
    public ResponseEntity<String> update(UserUpdateRequestDto dto, HttpServletRequest request) {
        userService.update(dto, request);
        return ResponseEntity.ok("회원 업데이트 완료!");
    }

    @GetMapping("/mypage")
    @Operation(summary = "유저정보 페이지")
    public ResponseEntity<UserMyPageDto> getUserInfo(HttpServletRequest request) {
        UserMyPageDto userInfo = userService.viewUserInfo(request);
        return ResponseEntity.ok(userInfo);
    }


    @GetMapping("/withdraw")
    @Operation(summary = "회원 탈퇴")
    public ResponseEntity<String> withdraw(HttpServletRequest request) {
        userService.withdrawUser(request);
        return ResponseEntity.ok("회원 탈퇴 완료!");
    }

    @GetMapping("/reissue")
    @Operation(summary = "RT 재발급 api")
    public ResponseEntity<String> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        userService.reissueToken(request, response);
        return ResponseEntity.ok("토큰 재발급 완료!");
    }

}