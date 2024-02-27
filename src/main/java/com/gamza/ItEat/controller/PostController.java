package com.gamza.ItEat.controller;

import com.gamza.ItEat.dto.post.*;
import com.gamza.ItEat.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.junit.runners.Parameterized;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping  // 모든 게시물 조회
    public ResponsePostListDto findAll() {
        ResponsePostListDto all = postService.findAllPost();
        return all;
    }

    @GetMapping("/{category}") // 카테고리별 게시물 조회
    public PaginationDto findPostByCategory(
            @PathVariable Long category,
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "8") // 8개씩
            int size) {
        PaginationDto allPostByParentCategory = postService.findPostByCategoryId(category, PageRequest.of(page, size));
        return allPostByParentCategory;
    }

    @GetMapping("/like")
    public List<ResponsePostDto> getPostLowerThanId(@RequestParam Long lastPostId, @RequestParam int size) {
        return postService.findAllPostByLogic(lastPostId, size);
    }

    @PostMapping()
    public ResponseEntity<String> createPost(@RequestBody RequestPostDto dto, HttpServletRequest request) {
        postService.createPost(dto, request);
        return ResponseEntity.ok().body("게시물이 생성되었습니다.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePost(@RequestBody RequestUpdatePostDto dto, @PathVariable("id") Long id, HttpServletRequest request) {
        postService.updatePost(dto, id, request);
        return ResponseEntity.ok().body("게시물이 업데이트 되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") Long id, HttpServletRequest request) {
        postService.deletePost(id, request);
        return ResponseEntity.ok().body("게시물이 삭제되었습니다.");
    }

}
