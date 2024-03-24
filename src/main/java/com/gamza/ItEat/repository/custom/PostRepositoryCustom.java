package com.gamza.ItEat.repository.custom;

import com.gamza.ItEat.enums.TagName;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepositoryCustom {
    List<Long> findPostIdsByTags(List<TagName> tags);
}
