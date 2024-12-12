package com.example.social_network.controllers;

import com.example.social_network.dtos.Request.PostRequest;
import com.example.social_network.dtos.Response.PostResponse;
import com.example.social_network.dtos.Response.PostResponseDetail;
import com.example.social_network.response.Error;
import com.example.social_network.response.Response;
import com.example.social_network.services.Iservice.IPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/post")
@RequiredArgsConstructor
public class PostController {
    private final IPostService iPostService;

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@Valid @ModelAttribute PostRequest postRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String error = errorMessages.toString();
            Error errorResponse = new Error(error, "422");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        PostResponse createdPost = iPostService.createPost(postRequest);
        Response<Object> response = Response.builder()
                .message("Bài viết đã được tạo thành công")
                .data(createdPost)
                .success(true)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable String postId) {
        PostResponseDetail postResponseDetail = iPostService.getPostById(postId);

        Response<Object> response = Response.builder()
                .message("Lấy chi tiết bài viết thành công")
                .data(postResponseDetail)
                .success(true)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable String postId) {
        iPostService.deletePost(postId);

        Response<Object> response = Response.builder()
                .message("Bài viết đã được xoá thành công")
                .success(true)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

