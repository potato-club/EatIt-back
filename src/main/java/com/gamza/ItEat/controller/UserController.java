package com.gamza.ItEat.controller;

import com.gamza.ItEat.dto.post.PaginationDto;
import com.gamza.ItEat.dto.user.LoginRequestDto;
import com.gamza.ItEat.dto.user.LoginResponseDto;
import com.gamza.ItEat.dto.user.SignUpRequestDto;
import com.gamza.ItEat.service.PostService;
import com.gamza.ItEat.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
@Tag(name = "User Controller", description = "User API")

public class UserController {

    private final UserService userService;

    @Operation(summary = "사용자 로그인")
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        return userService.login(requestDto, response);
    }

    @Operation(summary = "사용자 회원가입")
    @PostMapping("/signup")
    public ResponseEntity<String> userSignUp(@RequestBody SignUpRequestDto requestDto, HttpServletResponse response) {
        userService.signUp(requestDto, response);
        return ResponseEntity.ok("회원가입 완료!");
    }

    @Operation(summary = "사용자 로그아웃")
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        userService.logout(request);
        return ResponseEntity.ok("로그아웃 완료!");
    }

//    @Operation(summary = "해시태그 검색")
//    @GetMapping("/search")
//    public PaginationDto searchPostByTags(
//            @RequestParam(value = "tag") List<String> tag,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "8") int size) {
////        PaginationDto searchPostByTags = userService.
//    }

}
