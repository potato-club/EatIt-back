package com.gamza.ItEat.repository;

import com.gamza.ItEat.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findTop5ByOrderByCreatedAtDesc();
    Page<PostEntity> findByIdLessThanOrderByIdDesc(Long lastPostId, PageRequest pageRequest);  // 나중에 바꿔야겠는데 조회순, 추천순으로

}
