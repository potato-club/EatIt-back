package com.gamza.ItEat.entity;

import com.gamza.ItEat.enums.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "tag_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag_name")
    private Tag tag;

}
