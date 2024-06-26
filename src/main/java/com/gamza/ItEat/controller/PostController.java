package com.gamza.ItEat.controller;

import com.gamza.ItEat.dto.post.*;
import com.gamza.ItEat.enums.TagName;
import com.gamza.ItEat.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Tag(name = "Post Controller", description = "Post API")
public class PostController {

    private final PostService postService;

    @Operation(summary = "모든 게시물 조회")
    @GetMapping  // 모든 게시물 조회
    public ResponsePostListDto findAll() {
        ResponsePostListDto all = postService.findAllPost();
        return all;
    }

    @Operation(summary = "한개의 게시물 조회")
    @GetMapping("/find/{id}") // 게시물하나
    public ResponsePostDto findOnePost(@PathVariable("id") Long id) {
        ResponsePostDto one = postService.findOnePost(id);
        return one;
    }

    @Operation(summary = "카테고리별 게시물 조회")
    @GetMapping("/category/{categoryId}") // 카테고리별 게시물 조회 //
    public PaginationDto findPostByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "8") // 8개씩
            int size) {
        PaginationDto allPostByParentCategory = postService.findPostByCategoryId(categoryId, PageRequest.of(page, size));
        return allPostByParentCategory;
    }

    @Operation(summary = "Hot한 게시물 조회(좋아요순)")
    @GetMapping("/like")
    public List<ResponsePostDto> getPostLowerThanId(@RequestParam Long lastPostId, @RequestParam int size) {
        return postService.findAllPostByLogic(lastPostId, size);
    }

    @Operation(summary = "게시물 생성 (유저권한 필요)")
    @PostMapping()
    public ResponseEntity<String> createPost(@RequestBody RequestPostDto dto, HttpServletRequest request) {
        postService.createPost(dto, request);
        return ResponseEntity.ok().body("게시물이 생성되었습니다.");
    }

    @Operation(summary = "게시물 수정 (유저권한 필요)")
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePost(@RequestBody RequestUpdatePostDto dto, @PathVariable("id") Long id, HttpServletRequest request) {
        postService.updatePost(dto, id, request);
        return ResponseEntity.ok().body("게시물이 업데이트 되었습니다.");
    }

    @Operation(summary = "게시물 삭제 (유저권한 필요)")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") Long id, HttpServletRequest request) {
        postService.deletePost(id, request);
        return ResponseEntity.ok().body("게시물이 삭제되었습니다.");
    }

    @Operation(summary = "게시물 조회수 조회") // 이거는 그냥 게시물 조회에 넣어야하는건가?
    @GetMapping("/{id}")
    public int getPostViews(@PathVariable("id") Long id) {
        return postService.getPostViews(id);
    }

    @Operation(summary = "모든 태그 반환 API")
    @GetMapping("/tag")
    public List<TagInfoDto> getAllTags() {
        return postService.getAllTagsId();
    }

    @Operation(summary = "게시물 즐겨찾기 (유저권한 필요)")
    @PostMapping("/subscribe/{id}")
    public ResponseEntity<String> setSubscribe(@PathVariable("id") Long postId, HttpServletRequest request, @RequestBody SubscribeDto dto) {
        return postService.subscribePost(postId, dto, request);
    }

}
