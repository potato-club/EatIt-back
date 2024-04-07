package com.gamza.ItEat.repository;

import com.gamza.ItEat.entity.PostEntity;
import com.gamza.ItEat.entity.SubscribeEntity;
import com.gamza.ItEat.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeRepository extends JpaRepository<SubscribeEntity, Long> {
    SubscribeEntity findByUserAndPost(UserEntity user, PostEntity post);
}
