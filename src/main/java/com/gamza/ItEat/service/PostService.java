package com.gamza.ItEat.service;

import com.gamza.ItEat.dto.post.*;
import com.gamza.ItEat.entity.CategoryEntity;
import com.gamza.ItEat.entity.PostEntity;
import com.gamza.ItEat.entity.UserEntity;
import com.gamza.ItEat.enums.UserRole;
import com.gamza.ItEat.error.ErrorCode;
import com.gamza.ItEat.error.exeption.BadRequestException;
import com.gamza.ItEat.error.exeption.NotFoundException;
import com.gamza.ItEat.error.exeption.UnAuthorizedException;
import com.gamza.ItEat.repository.CategoryRepository;
import com.gamza.ItEat.repository.PostRepository;
import com.gamza.ItEat.utils.ResponseValue;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public ResponsePostDto findOnePost(Long id) {
        Optional<PostEntity> postOptional = postRepository.findById(id);
        PostEntity post = postOptional.orElseThrow(() -> new NoSuchElementException("게시물이 존재하지 않습니다."));
        return ResponseValue.getOneBuild(post);
    }

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

    public List<ResponsePostDto> findAllPostByLogic(Long lastPostId, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        Page<PostEntity> entityPage = postRepository.findByIdLessThanOrderByIdDesc(lastPostId, pageRequest);
        List<PostEntity> postEntityList = entityPage.getContent();

        return postEntityList.stream()
                .map(ResponseValue::getAllBuild)
                .collect(Collectors.toList());
    }

    public PaginationDto findPostByCategoryId(Long categoryId, Pageable pageable) {
        Optional<CategoryEntity> id = categoryRepository.findById(categoryId);
        CategoryEntity categoryName = categoryRepository.findByCategoryName(id.get().getCategoryName());

        if (categoryName == null) {
            throw new BadRequestException("카테고리를 찾을 수 없습니다.", ErrorCode.NOT_FOUND_EXCEPTION);
        }

        List<PostEntity> postList = id.stream()
                .flatMap(category -> category.getPosts().stream())
                .sorted(Comparator.comparing(PostEntity::getCreatedAt).reversed())
                .collect(Collectors.toList());

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        int endItem = Math.min(startItem + pageSize, postList.size());

        List<PostEntity> pageContent = postList.subList(startItem, endItem);
        List<ResponsePostDto> postDtos = pageContent.stream()
                .map(ResponseValue::getAllBuild)
                .collect(Collectors.toList());

        long totalPages = (long) Math.ceil((double) postList.size() / (double) pageSize);
        boolean isLastPage = !pageable.isPaged() || currentPage >= totalPages - 1;

        return ResponseValue.getPaginationDto(totalPages, isLastPage, (long) postList.size(), postDtos);

    }


    public Long createPost(RequestPostDto requestDto, HttpServletRequest request) {

        Optional<UserEntity> userEntity = userService.findByUserToken(request);
        if (userEntity.get().getUserRole() != UserRole.USER) {
            throw new UnAuthorizedException("유저 권한이 없습니다.", ErrorCode.ACCESS_DENIED_EXCEPTION);
        } else {

            CategoryEntity category = categoryRepository.findByCategoryName(requestDto.getCategoryName());

            if (category == null) {
                throw new NotFoundException("카테고리를 찾을 수 없습니다.", ErrorCode.NOT_FOUND_EXCEPTION);
            }

            PostEntity post = PostEntity.builder()
                    .title(requestDto.getTitle())
                    .content(requestDto.getContent())
                    .category(category)
                    .build();
            PostEntity savedPost = postRepository.save(post); // 게시물 저장하고

            return savedPost.getId(); // 게시물 id값 반환
        }
    }

    public Long updatePost(RequestUpdatePostDto updatePostDto, Long id, HttpServletRequest request) {
        Optional<UserEntity> userEntity = userService.findByUserToken(request);

        if (userEntity.get().getUserRole() != UserRole.USER) {
            throw new UnAuthorizedException("유저 권한이 없습니다.", ErrorCode.ACCESS_DENIED_EXCEPTION);
        } else {
            PostEntity originPost = postRepository.findById(id).
                    orElseThrow(() -> new BadRequestException("게시물이 존재하지 않습니다.", ErrorCode.RUNTIME_EXCEPTION)); // 오류 출력 게시물 없을떄 따로하나 만들어야겠다.

            // 카테고리도 수정할수있게 해줘야하나 ? 흠

            String updatedTitle = updatePostDto.getTitle();
            String updatedContent = updatePostDto.getContent();

            originPost.updatePost(updatedTitle, updatedContent);
            return postRepository.save(originPost).getId();
        }
    }

    public void deletePost(Long id, HttpServletRequest request) {
        Optional<UserEntity> userEntity = userService.findByUserToken(request);

        if (userEntity.get().getUserRole() != UserRole.USER) {
            throw new UnAuthorizedException("유저 권한이 없습니다.", ErrorCode.ACCESS_DENIED_EXCEPTION);
        } else {

            PostEntity originPost = postRepository.findById(id).
                    orElseThrow(() -> new BadRequestException("게시물이 존재하지 않습니다.", ErrorCode.RUNTIME_EXCEPTION));

            postRepository.deleteById(originPost.getId());
        }
    }

    public void getPostView(Long id) {
        Optional<PostEntity> post = postRepository.findById(id);
        if (post.isPresent()) {
            PostEntity postEntity = post.get();
            postEntity.increaseViews();
            postRepository.save(postEntity);
        } else {
            throw new NotFoundException("잘못된 접근입니다.", ErrorCode.ACCESS_DENIED_EXCEPTION);
        }
    }


}
