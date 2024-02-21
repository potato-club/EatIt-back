package com.gamza.ItEat.controller;

import com.gamza.ItEat.dto.post.PaginationDto;
import com.gamza.ItEat.dto.post.RequestPostDto;
import com.gamza.ItEat.dto.post.RequestUpdatePostDto;
import com.gamza.ItEat.dto.post.ResponsePostListDto;
import com.gamza.ItEat.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.junit.runners.Parameterized;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponsePostListDto findAll() {
        ResponsePostListDto all = postService.findAllPost();
        return all;
    }

    @GetMapping("/{categoryId}")
    public PaginationDto findPostByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "4")
            int size) {
        PaginationDto allPostByParentCategory = postService.findPostByCategoryId(categoryId, PageRequest.of(page, size));
        return allPostByParentCategory;

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
