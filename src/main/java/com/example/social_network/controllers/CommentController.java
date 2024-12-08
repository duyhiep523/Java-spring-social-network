package com.example.social_network.controllers;

import com.example.social_network.dtos.Request.CommentRequest;
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


//    // Xóa bình luận
//    @DeleteMapping("/delete/{commentId}")
//    public ResponseEntity<?> deleteComment(@PathVariable String commentId) {
//        commentService.deleteComment(commentId);
//
//        Response<Object> response = Response.builder()
//                .message("Bình luận đã được xóa thành công")
//                .success(true)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }
//
//    // Lấy danh sách bình luận của bài viết
//    @GetMapping("/list/{postId}")
//    public ResponseEntity<?> getCommentsForPost(@PathVariable String postId) {
//        List<CommentResponse> comments = commentService.getCommentsForPost(postId);
//
//        Response<List<CommentResponse>> response = Response.<List<CommentResponse>>builder()
//                .message("Danh sách bình luận của bài viết")
//                .data(comments)
//                .success(true)
//                .build();
//
//        return ResponseEntity.ok(response);
//    }
//
//    // Lấy số lượng bình luận của bài viết
//    @GetMapping("/count/{postId}")
//    public ResponseEntity<?> countCommentsForPost(@PathVariable String postId) {
//        long commentCount = commentService.countCommentsForPost(postId);
//
//        Response<Long> response = Response.<Long>builder()
//                .message("Số lượng bình luận của bài viết")
//                .data(commentCount)
//                .success(true)
//                .build();
//
//        return ResponseEntity.ok(response);
//    }

}
