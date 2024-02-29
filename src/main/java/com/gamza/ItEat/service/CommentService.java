package com.gamza.ItEat.service;

import com.gamza.ItEat.dto.comment.CommentRequestDto;
import com.gamza.ItEat.dto.comment.CommentResponseDto;
import com.gamza.ItEat.entity.CommentEntity;
import com.gamza.ItEat.entity.PostEntity;
import com.gamza.ItEat.entity.UserEntity;
import com.gamza.ItEat.error.ErrorCode;
import com.gamza.ItEat.error.exeption.NotFoundException;
import com.gamza.ItEat.error.exeption.UnAuthorizedException;
import com.gamza.ItEat.repository.CommentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    public List<CommentResponseDto> findAllCommentByPost(Long id,Long lastCommentId, int size) {

        Long postId = postService.getPostId(id).getId();

        if(postId != null) {
            PageRequest pageRequest = PageRequest.of(0, size);
            Page<CommentEntity> entityPage = commentRepository.findByPostIdAndIdLessThanOrderByIdDesc(postId, lastCommentId, pageRequest);
            List<CommentEntity> commentEntityList = entityPage.getContent();

            List<CommentResponseDto> commentResponseDto = commentEntityList.stream()
                    .map(commentEntity -> CommentResponseDto.builder()
                            .id(commentEntity.getId())
                            .postId(postId)
                            .content(commentEntity.getContent())
                            .userName(commentEntity.getUser().getNickName())
                            .createdAt(commentEntity.getCreatedAt())
                            .build())
                    .collect(Collectors.toList());
            return commentResponseDto;
        } else {
            return Collections.emptyList();
        }
    }

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

    public void deleteComment(Long id, HttpServletRequest request) {
        Optional<UserEntity> user = userService.findByUserToken(request);

        if (user.get().getUserRole() == null) {
            throw new UnAuthorizedException("로그인후 이용해주세요.", ErrorCode.NOT_ALLOW_WRITE_EXCEPTION);
        } else {
            CommentEntity commentId = commentRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("댓글이 존재하지않습니다.", ErrorCode.NOT_FOUND_EXCEPTION));

            commentRepository.deleteById(commentId.getId());
        }

    }
}
