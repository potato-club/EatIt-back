package com.gamza.ItEat.entity;

import com.gamza.ItEat.dto.user.UserUpdateRequestDto;
import com.gamza.ItEat.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(nullable = false)
    private boolean deleted;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean emailOtp;

    @Column
    private String profile; // 프로필이 null일경우 기본사진 설정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity posts;

    @ManyToMany
    @JoinTable(
            name = "user_tags",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagEntity> tags;

//    @ManyToOne(fetch = FetchType.LAZY) // 이런식으로 관계설정을 하면 되려나?
//    @JoinColumn(name = "subscribe")
//    private PostEntity subscribePost;

    public void setDeleted(boolean deleted) { // 아니 이거 여기도있네 왜여기씀
        this.deleted = deleted;
    }
    public void update(UserUpdateRequestDto userUpdateDto) {
        this.nickName = userUpdateDto.getNickname();
        this.email = userUpdateDto.getEmail();
        this.userRole = userUpdateDto.getUserRole();
        this.tags = userUpdateDto.toTagEntities();

    }

    public void setEmailOtp(boolean emailOtp) {
        this.emailOtp = emailOtp;
    }

}