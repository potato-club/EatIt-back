package com.gamza.ItEat.service;


import com.gamza.ItEat.dto.user.LoginRequestDto;
import com.gamza.ItEat.dto.user.SignUpRequestDto;
import com.gamza.ItEat.entity.UserEntity;
import com.gamza.ItEat.enums.UserRole;
import com.gamza.ItEat.error.ErrorCode;
import com.gamza.ItEat.error.exeption.UnAuthorizedException;
import com.gamza.ItEat.jwt.JwtProvider;
import com.gamza.ItEat.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;


    public ResponseEntity<String> signUp(SignUpRequestDto signupRequestDto, HttpServletResponse response) {
        if (userRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일 입니다.");
        }

        String encryptedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        UserEntity userEntity = UserEntity.builder()
                .nickName(signupRequestDto.getNickName())
                .email(signupRequestDto.getEmail())
                .password(encryptedPassword)
                .userRole(UserRole.USER)
                .refreshToken("dummy")
                .build();

        userRepository.save(userEntity);

        String email = signupRequestDto.getEmail();
        UserRole userRole = userRepository.findByEmail(email).getUserRole();

        String AT = jwtProvider.createAccessToken(email, userRole);
        String RT = jwtProvider.createRefreshToken(email, userRole);

        userEntity.setRefreshToken(RT);

        response.setHeader("Authorization", "Bearer " + AT);
        response.setHeader("RefreshToken", "Bearer " + RT);

        return ResponseEntity.ok("회원가입 성공");
    }

    public ResponseEntity<String> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        UserEntity userEntity = userRepository.findByEmail(loginRequestDto.getEmail());

        if(userEntity == null) {
            throw new UnAuthorizedException(ErrorCode.INVALID_ACCESS.getMessage(), ErrorCode.INVALID_ACCESS);
        }
//                .orElseThrow(() -> new UnAuthorizedException(ErrorCode.INVALID_ACCESS.getMessage(), ErrorCode.INVALID_ACCESS));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), userEntity.getPassword())) {
            throw new UnAuthorizedException(ErrorCode.INVALID_ACCESS.getMessage(), ErrorCode.INVALID_ACCESS);
        }

        String AT = jwtProvider.createAccessToken(userEntity.getEmail(), userEntity.getUserRole());
        String RT = jwtProvider.createRefreshToken(userEntity.getEmail(), userEntity.getUserRole());

        userEntity.setRefreshToken(RT);

        response.setHeader("Authorization", "Bearer " + AT);
        response.setHeader("RefreshToken", "Bearer " + RT);

        return ResponseEntity.ok("로그인 성공");
    }

    public UserEntity findByUserToken(HttpServletRequest request) {
        String token = jwtProvider.resolveAccessToken(request);
        String accessTokenType = jwtProvider.extractTokenType(token);

        if("refresh".equals(accessTokenType)) {
            throw new UnAuthorizedException("RefreshToken은 사용할 수 없습니다.", ErrorCode.INVALID_TOKEN_EXCEPTION);
        }

        return token == null ? null : userRepository.findByEmail(jwtProvider.getUserEmail(token));
    }

}

