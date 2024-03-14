package com.gamza.ItEat.repository;

import com.gamza.ItEat.entity.CommentEntity;
import com.gamza.ItEat.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findByPostIdAndIdLessThanOrderByIdDesc(Long post, Long lastCommentId, PageRequest pageRequest);
}
