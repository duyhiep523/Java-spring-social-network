package com.example.social_network.controllers;

import com.example.social_network.comon.enums.PostReactionEnum;
import com.example.social_network.dtos.Request.PostReactionResquest;
import com.example.social_network.dtos.Response.PostReactionResponse;
import com.example.social_network.dtos.Response.UserReactionResponse;
import com.example.social_network.response.Error;
import com.example.social_network.response.Response;
import com.example.social_network.services.Iservice.IPostReactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/postReaction")
@RequiredArgsConstructor
public class PostReactionController {
    @Autowired
    private IPostReactionService postReactionService;

    @PostMapping("add")
    public ResponseEntity<?> addReaction(
            @Valid @RequestBody PostReactionResquest postReactionResquest,
            BindingResult result) {

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String error = errorMessages.toString();
            Error errorResponse = new Error(error, "422");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        PostReactionResponse createdReaction = postReactionService.addReaction(
                postReactionResquest.getPostId(),
                postReactionResquest.getUserId(),
                postReactionResquest.getReactionType()
        );

        Response<Object> response = Response.builder()
                .message("Phản ứng đã được thêm thành công")
                .data(createdReaction)
                .success(true)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("delete/{postId}/{userId}")
    public ResponseEntity<?> deleteReaction(@PathVariable String postId, @PathVariable String userId) {
        postReactionService.deleteReaction(postId, userId);
        Response<Object> response = Response.builder()
                .message("Phản ứng đã được xóa thành công")
                .success(true)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("reactions/{postId}")
    public ResponseEntity<?> getReactionsForPost(@PathVariable String postId) {
        List<UserReactionResponse> userReactions = postReactionService.getReactionsForPost(postId);
        Response<List<UserReactionResponse>> response = Response.<List<UserReactionResponse>>builder()
                .message("Danh sách người dùng và cảm xúc đã thả cho bài viết")
                .data(userReactions)
                .success(true)
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("count-reactions/{postId}")
    public ResponseEntity<?> countReactionsForPost(@PathVariable String postId) {
        long reactionCount = postReactionService.countReactionsForPost(postId);
        Response<Long> response = Response.<Long>builder()
                .message("Số lượng lượt cảm xúc đã thả cho bài viết")
                .data(reactionCount)
                .success(true)
                .build();

        return ResponseEntity.ok(response);
    }
}
