package com.gamza.ItEat.controller;

import com.gamza.ItEat.dto.comment.CommentRequestDto;
import com.gamza.ItEat.dto.comment.CommentResponseDto;
import com.gamza.ItEat.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@Tag(name = "Comment Controller", description = "Comment API")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "게시물별 모든 댓글 조회")
    @GetMapping("/{id}")
    public List<CommentResponseDto> getCommentLowerThanIdByPost(@PathVariable("id") Long id, @RequestParam Long lastCommentId, @RequestParam int size) {
        return commentService.findAllCommentByPost(id, lastCommentId, size);
    }

    @Operation(summary = "댓글 생성 (유저권한 필요)")
    @PostMapping("/{id}") // 게시물 id값
    public ResponseEntity<String> createComment(@RequestBody CommentRequestDto dto, @PathVariable("id") Long id, HttpServletRequest request) {
        commentService.createComment(id, dto, request);
        return ResponseEntity.ok().body("댓글이 생성되었습니다.");
    }

    @Operation(summary = "댓글 수정 (유저권한 필요)")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateComment(@RequestBody CommentRequestDto dto, @PathVariable("id") Long id, HttpServletRequest request) {
        commentService.updateComment(id,dto,request);
        return ResponseEntity.ok().body("댓글이 수정되었습니다.");
    }

    @Operation(summary = "댓글 삭제 (유저권한 필요)")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") Long id, HttpServletRequest request) {
        commentService.deleteComment(id, request);
        return ResponseEntity.ok().body("댓글이 삭제되었습니다.");
    }
}
