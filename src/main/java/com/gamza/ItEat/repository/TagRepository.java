package com.gamza.ItEat.repository;

import com.gamza.ItEat.entity.TagEntity;
import com.gamza.ItEat.enums.TagName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

    List<TagEntity> findByTagIn(List<TagName> tags);

}
