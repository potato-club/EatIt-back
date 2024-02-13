package com.gamza.ItEat.controller;

import com.gamza.ItEat.dto.user.LoginRequestDto;
import com.gamza.ItEat.dto.user.LoginResponseDto;
import com.gamza.ItEat.dto.user.SignUpRequestDto;
import com.gamza.ItEat.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
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

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        userService.logout(request);
        return ResponseEntity.ok("로그아웃 완료!");
    }
}
