package com.gamza.ItEat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int likes;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CommentEntity comment;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @OneToMany(mappedBy = "comment")
    private List<CommentEntity> subComments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;
}