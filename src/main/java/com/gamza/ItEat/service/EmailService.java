package com.gamza.ItEat.service;

import com.gamza.ItEat.entity.UserEntity;
import com.gamza.ItEat.error.ErrorCode;
import javax.mail.internet.InternetAddress;
import com.gamza.ItEat.repository.UserRepository;
import com.gamza.ItEat.service.jwt.RedisService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Random;

@Service
@Transactional
public class EmailService {

    private final JavaMailSender gmailSender;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private final UserService userService;

    @Value("${email.gmail.id}")
    private String gmailUsername;


    @Autowired
    public EmailService(@Qualifier("gmail") JavaMailSender gmailSender,
                            RedisService redisJwtService,
                            UserRepository userRepository,
                            UserService userService) {
        this.gmailSender = gmailSender;
        this.redisService = redisJwtService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public void sendGmail(String recipientEmail) throws MessagingException, UnsupportedEncodingException {
        redisService.deleteExistingOtp(recipientEmail); // 먼저 요청한 otp code 가 있었다면 제거함.
        MimeMessage message = gmailSender.createMimeMessage();

        String ePw = createKey();
        commonMessage(recipientEmail, message, "gmail", ePw);

        redisService.setEmailOtpDataExpire(ePw, recipientEmail, 60 * 5L);   // 유효 시간 5분
        gmailSender.send(message);
    }

    public void verifyEmail(String key, HttpServletResponse response) {
        String email = redisService.getEmailOtpData(key).toString();

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new NotFoundException("Email Not Found");
        });

        user.setEmailOtp(true);
        user.setDeleted(false);

        redisService.deleteEmailOtpData(key);
        userService.setJwtTokenInHeader(email, response);
    }

    private void commonMessage(String recipientEmail, MimeMessage message, String type, String ePw) throws MessagingException, UnsupportedEncodingException {

        message.addRecipients(MimeMessage.RecipientType.TO, recipientEmail); // to 보내는 대상
        message.setSubject("잇잇 인증 코드: "); //메일 제목

        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면에서 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += ePw;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html"); // 내용, charset 타입, subType

        if (type.equals("gmail")) {
            message.setFrom(new InternetAddress(gmailUsername, "DG_Admin")); // 보내는 사람의 메일 주소, 보내는 사람 이름
        }
    }

    public static String createKey() {
        SecureRandom rnd = new SecureRandom();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            key.append(rnd.nextInt(10));
        }
        return key.toString();
    }




}
