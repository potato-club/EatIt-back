package com.gamza.ItEat.dto.user;

import com.gamza.ItEat.entity.UserEntity;
import com.gamza.ItEat.enums.UserRole;
import lombok.Data;

@Data
public class SignUpRequestDto {

    private String nickName;
    private String email;
    private UserRole userRole;
    private String password;

    public UserEntity toEntity() {

        return UserEntity.builder()
                .email(email)
                .password(password)
                .nickName(nickName)
                .userRole(UserRole.USER)
                .deleted(false)
                .build();
    }
}
