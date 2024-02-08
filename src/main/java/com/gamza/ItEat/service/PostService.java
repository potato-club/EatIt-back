package com.gamza.ItEat.service;

import com.gamza.ItEat.dto.post.RequestPostDto;
import com.gamza.ItEat.dto.post.RequestUpdatePostDto;
import com.gamza.ItEat.dto.post.ResponsePostDto;
import com.gamza.ItEat.dto.post.ResponsePostListDto;
import com.gamza.ItEat.entity.PostEntity;
import com.gamza.ItEat.error.ErrorCode;
import com.gamza.ItEat.error.exeption.BadRequestException;
import com.gamza.ItEat.repository.PostRepository;
import com.gamza.ItEat.utils.ResponseValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public ResponsePostListDto findAllPost() {

        List<PostEntity> all = postRepository.findTop5ByOrderByCreatedAtDesc();

        if (all.isEmpty()) {
            return ResponsePostListDto.builder()
                    .size(0)
                    .posts(Collections.emptyList())
                    .build();
        } else {

            List<ResponsePostDto> collect = all.stream()
                    .filter(Objects::nonNull)
                    .map(ResponseValue::getAllBuild)
                    .collect(Collectors.toList());

            return ResponsePostListDto.builder()
                    .size(all.size())
                    .posts(collect)
                    .build();
        }
    }

    public Long createPost(RequestPostDto requestDto) {
        PostEntity post = PostEntity.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();
        PostEntity savedPost = postRepository.save(post); // 게시물 저장하고

        return savedPost.getId(); // 게시물 id값 반환
    }

    public Long updatePost(RequestUpdatePostDto updatePostDto, Long id) {
        PostEntity originPost = postRepository.findById(id).
                orElseThrow(() -> new BadRequestException("게시물이 존재하지 않습니다.", ErrorCode.RUNTIME_EXCEPTION)); // 오류 출력 게시물 없을떄 따로하나 만들어야겠다.

        String updatedTitle = updatePostDto.getTitle();
        String updatedContent = updatePostDto.getContent();

        originPost.updatePost(updatedTitle, updatedContent);
        return postRepository.save(originPost).getId();
    }

    public void deletePost(Long id) {

        PostEntity originPost = postRepository.findById(id).
                orElseThrow(() -> new BadRequestException("게시물이 존재하지 않습니다.",ErrorCode.RUNTIME_EXCEPTION));

        postRepository.deleteById(id);
    }


}
