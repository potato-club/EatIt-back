package com.gamza.ItEat.repository;

import com.gamza.ItEat.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByEmailAndDeleted(String email, boolean deleted);
    boolean existsByEmailAndDeletedIsTrue(String email);
}
