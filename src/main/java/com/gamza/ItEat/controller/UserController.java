package com.gamza.ItEat.controller;

import com.gamza.ItEat.dto.user.*;
import com.gamza.ItEat.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        return userService.login(requestDto, response);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> userSignUp(@RequestBody SignUpRequestDto requestDto, HttpServletResponse response) {
        userService.signUp(requestDto, response);
        return ResponseEntity.ok("회원가입 완료!");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        userService.logout(request);
        return ResponseEntity.ok("로그아웃 완료!");
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(UserUpdateRequestDto dto, HttpServletRequest request) {
        userService.update(dto, request);
        return ResponseEntity.ok("회원 업데이트 완료!");
    }

    @GetMapping("/mypage")
    public ResponseEntity<UserMyPageDto> getUserInfo(HttpServletRequest request) {
        UserMyPageDto userInfo = userService.viewUserInfo(request);
        return ResponseEntity.ok(userInfo);
    }


    @GetMapping("/withdraw")
    public ResponseEntity<String> withdraw(HttpServletRequest request) {
        userService.withdrawUser(request);
        return ResponseEntity.ok("회원 탈퇴 완료!");
    }

    @GetMapping("/reissue")
    public ResponseEntity<String> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        userService.reissueToken(request, response);
        return ResponseEntity.ok("토큰 재발급 완료!");
    }

}