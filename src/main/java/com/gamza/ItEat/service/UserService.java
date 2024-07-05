package com.gamza.ItEat.service;

import com.gamza.ItEat.dto.user.*;
import com.gamza.ItEat.entity.TagEntity;
import com.gamza.ItEat.entity.UserEntity;
import com.gamza.ItEat.enums.UserRole;
import com.gamza.ItEat.error.ErrorCode;
import com.gamza.ItEat.error.exeption.UnAuthorizedException;
import com.gamza.ItEat.jwt.JwtProvider;
import com.gamza.ItEat.repository.TagRepository;
import com.gamza.ItEat.repository.UserRepository;
import com.gamza.ItEat.service.jwt.RedisService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gamza.ItEat.error.ErrorCode.ACCESS_DENIED_EXCEPTION;
import static com.gamza.ItEat.error.ErrorCode.NOT_FOUND_EXCEPTION;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto requestDto, HttpServletResponse response) {

        if (!userRepository.existsByEmailAndDeleted(requestDto.getEmail(), false)) {
            if (!userRepository.existsByEmail(requestDto.getEmail())) {
                return LoginResponseDto.builder()
                        .responseCode("2001")   // 회원이 아닐 경우
                        .build();
            } else if (userRepository.existsByEmailAndDeletedIsTrue(requestDto.getEmail())) {
                return LoginResponseDto.builder()
                        .responseCode("2002")   // 탈퇴한 회원인 경우
                        .build();
            }
        }

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
    public void signUp(SignUpRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new UnAuthorizedException("401", ACCESS_DENIED_EXCEPTION);
        }

        Set<TagEntity> tagEntities = requestDto.toTagEntities().stream()
                .map(tag -> tagRepository.findByTag(tag.getTag())
                        .orElseGet(() -> tagRepository.save(tag)))
                .collect(Collectors.toSet());

        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        UserEntity userEntity = requestDto.toEntity(tagEntities);
        userRepository.save(userEntity);
    }

    public void logout(HttpServletRequest request) {
        redisService.delValues(jwtProvider.resolveRefreshToken(request));
        jwtProvider.expireToken(jwtProvider.resolveAccessToken(request));
    }


    public UserMyPageDto viewUserInfo(HttpServletRequest request) throws IOException {
        Optional<UserEntity> userEntityOpt = findByUserToken(request);

        if (userEntityOpt.isPresent()) {
            UserEntity userEntity = userEntityOpt.get();
            return UserMyPageDto.builder()
                    .email(userEntity.getEmail())
                    .nickname(userEntity.getNickName())
                    .build();
        } else {
            throw new UnAuthorizedException("404", NOT_FOUND_EXCEPTION);
        }
    }

    public void update(UserUpdateRequestDto requestDto, HttpServletRequest request) {
        Optional<UserEntity> userOptional = findByUserToken(request);
        userOptional.ifPresent(user -> {
            if (requestDto.getNickname() != null && !requestDto.getNickname().isEmpty()) {
                user.update(requestDto);
            }
        });
    }


    public void withdrawUser(HttpServletRequest request) {
        Optional<UserEntity> user = findByUserToken(request);

        if (user.isPresent()) {
            UserEntity userEntity = user.get();
            userEntity.setDeleted(true);
            this.logout(request);
        } else {
            throw new UnAuthorizedException("404", NOT_FOUND_EXCEPTION);
        }
    }

    public void setJwtTokenInHeader(String email, HttpServletResponse response) {
        UserRole userRole = userRepository.findByEmail(email).get().getUserRole();

        String accessToken = jwtProvider.createAccessToken(email, userRole);
        String refreshToken = jwtProvider.createRefreshToken(email, userRole);


        jwtProvider.setHeaderAT(response, accessToken);
        jwtProvider.setHeaderRT(response, refreshToken);

        redisService.setValues(refreshToken, email);
    }


    public void reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtProvider.resolveRefreshToken(request);

        String newAccessToken = jwtProvider.reissueAccessToken(refreshToken);
        String newRefreshToken = jwtProvider.reissueRefreshToken(refreshToken);

        jwtProvider.setHeaderAT(response, newAccessToken);
        jwtProvider.setHeaderRT(response, newRefreshToken);
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
