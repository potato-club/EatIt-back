package com.gamza.ItEat.repository;

import com.gamza.ItEat.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findTop5ByOrderByCreatedAtDesc();

}
