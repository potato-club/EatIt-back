package com.gamza.ItEat.repository;

import com.gamza.ItEat.entity.TagEntity;
import com.gamza.ItEat.enums.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
}
