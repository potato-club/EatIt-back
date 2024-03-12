package com.gamza.ItEat.service;

import com.gamza.ItEat.dto.post.LikeDto;
import com.gamza.ItEat.entity.LikeEntity;
import com.gamza.ItEat.entity.PostEntity;
import com.gamza.ItEat.entity.UserEntity;
import com.gamza.ItEat.error.ErrorCode;
import com.gamza.ItEat.error.exeption.NotFoundException;
import com.gamza.ItEat.repository.LikeRepository;
import com.gamza.ItEat.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    public ResponseEntity<String> likeService(Long postId, LikeDto likeDto, HttpServletRequest request) {
        Optional<UserEntity> userEntity = userService.findByUserToken(request);
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("잘못된 요청입니다.", ErrorCode.RUNTIME_EXCEPTION));

        LikeEntity like = likeRepository.findByUserAndPost(userEntity.orElse(null), post);

        if(likeDto.isUnLiked()) {
            likeRepository.delete(like);
            post.increaseLikesNums();
            return ResponseEntity.ok().body("좋아요가 눌렸습니다.");
        } else {
            likeRepository.save(like);
            post.decreaseLikesNums();
            return ResponseEntity.ok().body("좋아요가 취소되었습니다.");
        }
    }

    private void decreaseLikeCount(Long postId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("잘못된 요청입니다.", ErrorCode.RUNTIME_EXCEPTION));
        int currentLikeCount = post.getLikesNum();

    }
}
