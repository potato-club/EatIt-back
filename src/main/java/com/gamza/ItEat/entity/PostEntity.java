package com.gamza.ItEat.entity;

import com.gamza.ItEat.enums.CategoryName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "post_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int likesNum;

    @Column(columnDefinition = "integer default 0", nullable = false) // 기본 조회수는 0
    private int views; // 게시물 조회수를 어떤식으로 count 할지?

    @Column(nullable = false)
    private int commentsNum;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int subscribeNum;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<FileEntity> images = new ArrayList<>();

    private boolean mentor;

    @ManyToMany
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagEntity> tags;


    public PostEntity updatePost(String title, String content, CategoryEntity category) {
        this.title = title;
        this.content = content;
        this.category = category;
        return this;
    }

    // 이렇게 아래처럼 안하고싶은데 흠 어떻게 나중에 리팩토링을 해야할까?

    public int increaseSubscribeNums() {
        return this.subscribeNum += 1;
    }

    public int decreaseSubscribeNums() {
        return this.subscribeNum -= 1;
    }


    public int increaseCommentNums() {
        return this.commentsNum += 1;
    }

    public int decreaseCommentNums() {
        return this.commentsNum -= 1;
    }

    public int increaseLikesNums() {
        return this.likesNum += 1;
    }

    public int decreaseLikesNums() {
        return this.likesNum -= 1;
    }

    public int increaseViews() {
        return this.views += 1;
    }

}