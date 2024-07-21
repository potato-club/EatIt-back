package com.gamza.ItEat.dto.user;

import com.gamza.ItEat.entity.TagEntity;
import com.gamza.ItEat.entity.UserEntity;
import com.gamza.ItEat.enums.TagName;
import com.gamza.ItEat.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Data

public class SignUpRequestDto {

    private String nickName;
    private String email;
    private String password;
    private String tags;

    public Set<TagEntity> toTagEntities() {
        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .map(tag -> new TagEntity(TagName.valueOf(tag)))
                .collect(Collectors.toSet());
    }

    // 수정된 메서드: UserEntity 생성 시 태그 엔티티들을 외부에서 받아 처리
    public UserEntity toEntity(Set<TagEntity> tagEntities) {
        return UserEntity.builder()
                .email(email)
                .password(password)
                .nickName(nickName)
                .tags(tagEntities)
                .userRole(UserRole.USER)
                .deleted(false)
                .build();
    }
}
