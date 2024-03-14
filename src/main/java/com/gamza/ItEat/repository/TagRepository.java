package com.gamza.ItEat.repository;

import com.gamza.ItEat.entity.TagEntity;
import com.gamza.ItEat.enums.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Long> {

    List<TagEntity> findByTagIn(List<Tag> tags);
//    List<TagEntity> findByTagIn(List<String> tags);
}
