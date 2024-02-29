package com.gamza.ItEat.service;

import com.gamza.ItEat.dto.comment.CommentRequestDto;
import com.gamza.ItEat.dto.comment.CommentResponseDto;
import com.gamza.ItEat.entity.CommentEntity;
import com.gamza.ItEat.entity.PostEntity;
import com.gamza.ItEat.entity.UserEntity;
import com.gamza.ItEat.error.ErrorCode;
import com.gamza.ItEat.error.exeption.UnAuthorizedException;
import com.gamza.ItEat.repository.CommentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    public CommentResponseDto createComment(Long postId, CommentRequestDto dto, HttpServletRequest request) {

        Optional<UserEntity> user = userService.findByUserToken(request);
        if (user.get().getUserRole() == null) {
            throw new UnAuthorizedException("로그인후 이용해주세요.", ErrorCode.NOT_ALLOW_WRITE_EXCEPTION);
        } else {
            PostEntity post = postService.getPostId(postId);

            CommentEntity comment = CommentEntity.builder()
                    .content(dto.getComment())
                    .post(post)
                    .user(user.get())
                    .build();
            CommentEntity save = commentRepository.save(comment);
            return new CommentResponseDto(save);
        }
    }
}
