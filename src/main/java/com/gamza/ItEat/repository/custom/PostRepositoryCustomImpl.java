package com.gamza.ItEat.repository.custom;

import com.gamza.ItEat.entity.QPostEntity;
import com.gamza.ItEat.entity.QTagEntity;
import com.gamza.ItEat.enums.TagName;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Long> findPostIdsByTags(List<TagName> tags) {
        QPostEntity qPost = QPostEntity.postEntity;
        QTagEntity qTag = QTagEntity.tagEntity;

        return jpaQueryFactory
                .select(qPost.id)
                .from(qPost)
                .innerJoin(qPost.tags, qTag)
                .where(qTag.tag.in(tags))
                .distinct()
                .fetch();
    }
}
