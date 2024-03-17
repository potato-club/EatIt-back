package com.gamza.ItEat.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UserMyPageDto {
    private String email;
    private String nickname;


}
