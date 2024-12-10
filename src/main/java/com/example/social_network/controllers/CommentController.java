package com.example.social_network.controllers;

import com.example.social_network.dtos.Request.CommentRequest;
import com.example.social_network.dtos.Request.CommentUpdateRequest;
import com.example.social_network.dtos.Response.CommentResponse;
import com.example.social_network.response.Error;
import com.example.social_network.response.Response;
import com.example.social_network.services.Iservice.IPostCommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/comments")
public class CommentController {
    @Autowired
    private IPostCommentService commentService;


    @PostMapping("/add")
    public ResponseEntity<?> addComment(@Valid @RequestBody CommentRequest commentRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String error = errorMessages.toString();
            Error errorResponse = new Error(error, "422");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        CommentResponse createdComment = commentService.addComment(
                commentRequest.getPostId(),
                commentRequest.getUserId(),
                commentRequest.getContent(),
                commentRequest.getParentCommentId()
        );

        Response<Object> response = Response.builder()
                .message("Bình luận đã được thêm thành công")
                .data(createdComment)
                .success(true)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getCommentsByPost(@PathVariable String postId) {
        List<CommentResponse> comments = commentService.getCommentsWithHierarchy(postId);

        Response<Object> response = Response.builder()
                .message("Lấy bình luận thành công")
                .data(comments)
                .success(true)
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/count/{postId}")
    public ResponseEntity<?> countCommentsByPost(@PathVariable String postId) {
        long commentCount = commentService.countCommentsByPost(postId);

        Response<?> response = Response.builder()
                .message("Số lượng bình luận của bài viết")
                .data(commentCount)
                .success(true)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editComment(@Valid @RequestBody CommentUpdateRequest commentUpdateRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String error = errorMessages.toString();
            Error errorResponse = new Error(error, "422");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        CommentResponse updatedComment = commentService.editComment(
                commentUpdateRequest.getCommentId(),
                commentUpdateRequest.getUserId(),
                commentUpdateRequest.getNewContent()
        );

        Response<?> response = Response.builder()
                .message("Bình luận đã được sửa thành công")
                .data(updatedComment)
                .success(true)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteComment(
            @RequestParam String commentId,
            @RequestParam String userId
    ) {
        commentService.deleteComment(commentId, userId);
        Response<Object> response = Response.builder()
                .message("Bình luận đã được xóa thành công")
                .data(null)
                .success(true)
                .build();

        return ResponseEntity.ok(response);
    }

}
