package com.gamza.ItEat.controller;


import com.gamza.ItEat.dto.email.requestEmailOtpDto;
import com.gamza.ItEat.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
@Tag(name = "Email Controller", description = "이메일 인증 API")
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "Gmail 인증 메일 발송 ")
    @PostMapping("/gmail")
    public ResponseEntity<String> sendGmail(@RequestBody requestEmailOtpDto emailDTO) throws UnsupportedEncodingException, MessagingException  {
        emailService.sendGmail(String.valueOf(emailDTO));
        return ResponseEntity.ok("인증 메일이 발송되었습니다.");

    }

    @Operation(summary = "인증 코드 확인 API")
    @PostMapping("/verify")
    public ResponseEntity<String> verifyEmail(String key, HttpServletResponse response) {
        emailService.verifyEmail(key, response);
        return ResponseEntity.ok("2차 인증이 정상적으로 처리되었습니다.");
    }
}
