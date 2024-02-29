package com.gamza.ItEat.controller;

import com.gamza.ItEat.dto.comment.CommentRequestDto;
import com.gamza.ItEat.dto.comment.CommentResponseDto;
import com.gamza.ItEat.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{id}")
    public List<CommentResponseDto> getCommentLowerThanIdByPost(@PathVariable("id") Long id, @RequestParam Long lastCommentId, @RequestParam int size) {
        return commentService.findAllCommentByPost(id, lastCommentId, size);
    }

    @PostMapping("/{id}") // 게시물 id값
    public ResponseEntity<String> createComment(@RequestBody CommentRequestDto dto, @PathVariable("id") Long id, HttpServletRequest request) {
        commentService.createComment(id, dto, request);
        return ResponseEntity.ok().body("댓글이 생성되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") Long id, HttpServletRequest request) {
        commentService.deleteComment(id, request);
        return ResponseEntity.ok().body("댓글이 삭제되었습니다.");
    }
}
