package com.gamza.ItEat.dto.user;

import com.gamza.ItEat.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UserUpdateRequestDto {

    private String email;
    private String nickname;
    private UserRole userRole;


}
