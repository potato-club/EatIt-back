package com.gamza.ItEat.controller;

import com.gamza.ItEat.dto.post.RequestPostDto;
import com.gamza.ItEat.dto.post.RequestUpdatePostDto;
import com.gamza.ItEat.dto.post.ResponsePostListDto;
import com.gamza.ItEat.service.PostService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping()
    public ResponseEntity<String> createPost(@RequestBody RequestPostDto dto) {
        postService.createPost(dto);
        return ResponseEntity.ok().body("게시물이 생성되었습니다.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePost(@RequestBody RequestUpdatePostDto dto, @PathVariable("id") Long id) {
        postService.updatePost(dto,id);
        return ResponseEntity.ok().body("게시물이 업데이트 되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") Long id){
        postService.deletePost(id);
        return ResponseEntity.ok().body("게시물이 삭제되었습니다.");
    }

}
