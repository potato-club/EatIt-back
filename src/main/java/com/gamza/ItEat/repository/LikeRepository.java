package com.gamza.ItEat.repository;

import com.gamza.ItEat.entity.CommentEntity;
import com.gamza.ItEat.entity.LikeEntity;
import com.gamza.ItEat.entity.PostEntity;
import com.gamza.ItEat.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
        LikeEntity findByUserAndPost(UserEntity user, PostEntity post);
        LikeEntity findByUserAndComment(UserEntity user, CommentEntity comment);


}
