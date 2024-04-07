package com.gamza.ItEat.repository;

import com.gamza.ItEat.entity.PostEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query("SELECT p FROM PostEntity p ORDER BY p.likesNum DESC")
    List<PostEntity> findAllOrderByLikesDesc();

//    Page<PostEntity> findByTagNames(@Param("tagNames") String tagNames, Pageable pageable);

    List<PostEntity> findTop5ByOrderByCreatedAtDesc();
    Page<PostEntity> findByIdLessThanOrderByIdDesc(Long lastPostId, PageRequest pageRequest);  // 나중에 바꿔야겠는데 조회순, 추천순으로

    Page<PostEntity> findByIdIn(List<Long> ids, Pageable pageable);

}
