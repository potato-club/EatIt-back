package com.gamza.ItEat.dto.user;

import com.gamza.ItEat.entity.UserEntity;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
public class SignUpRequestDto {

    private String nickName;
    private String email;
    private String password;


}
