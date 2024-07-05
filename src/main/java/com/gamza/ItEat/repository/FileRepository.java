package com.gamza.ItEat.repository;

import com.gamza.ItEat.entity.FileEntity;
import com.gamza.ItEat.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    void deleteByFileName(String storedName);
    List<FileEntity> findByPost(PostEntity post);
}
