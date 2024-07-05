package com.gamza.ItEat.dto.user;

import com.gamza.ItEat.entity.TagEntity;
import com.gamza.ItEat.entity.UserEntity;
import com.gamza.ItEat.enums.TagName;
import com.gamza.ItEat.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
public class UserUpdateRequestDto {

    private String email;
    private String nickname;
    private UserRole userRole;
    private String tags; // 콤마로 구분된 태그 문자열

    public Set<TagEntity> toTagEntities() {
        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .map(tag -> new TagEntity(TagName.valueOf(tag)))
                .collect(Collectors.toSet());
    }
}
