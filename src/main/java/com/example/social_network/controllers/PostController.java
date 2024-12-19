package com.example.social_network.controllers;

import com.example.social_network.dtos.Request.PostRequest;
import com.example.social_network.dtos.Request.PostUpdateRequest;
import com.example.social_network.dtos.Response.NewsFeed;
import com.example.social_network.dtos.Response.PostImageResponse;
import com.example.social_network.dtos.Response.PostResponse;
import com.example.social_network.dtos.Response.PostResponseDetail;
import com.example.social_network.entities.PostImage;
import com.example.social_network.response.Error;
import com.example.social_network.response.Response;
import com.example.social_network.services.Iservice.IPostImageService;
import com.example.social_network.services.Iservice.IPostService;
import com.example.social_network.services.PostImageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    private final IPostImageService iPostImageService;

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


    @PutMapping("/update/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable String postId, @Valid @ModelAttribute PostUpdateRequest postUpdateRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String error = errorMessages.toString();
            Error errorResponse = new Error(error, "422");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        PostResponse updatedPost = iPostService.updatePost(postId, postUpdateRequest);

        Response<Object> response = Response.builder()
                .message("Bài viết đã được cập nhật thành công")
                .data(updatedPost)
                .success(true)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostsByUserId(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {

       NewsFeed posts = iPostService.getAllPostsByUserIdFromFriend(userId, page, size);

        Response<Object> response = Response.builder()
                .message("Lấy danh sách bài viết thành công")
                .data(posts)
                .success(true)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<?> getPostsByUserIdWithoutFriendship(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        NewsFeed newsFeed = iPostService.getAllPostsByUserId(userId, page, size);
        Response<Object> response = Response.builder()
                .message("Lấy danh sách bài viết của bạn thành công")
                .data(newsFeed)
                .success(true)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @GetMapping("/ImageUser/{userId}")
    public ResponseEntity<?> getImagesByUserId(@PathVariable String userId) {
        List<PostImageResponse> images = iPostImageService.getAllImagesByUserId(userId);
        if (images.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Response<Object> response = Response.builder()
                .message("Lấy danh sách ảnh thành công")
                .data(images)
                .success(true)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/share/{postId}")
    public ResponseEntity<?> incrementNumOfShare(@PathVariable String postId) {
        iPostService.incrementNumOfShare(postId);

        Response<Object> response = Response.builder()
                .message("Cập nhật số lượt chia sẻ thành công")
                .success(true)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{postId}/share-count")
    public ResponseEntity<?> getShareCount(@PathVariable String postId) {
        int shareCount = iPostService.getShareCount(postId);

        Response<Object> response = Response.builder()
                .message("Lấy số lượt chia sẻ thành công")
                .data(shareCount)
                .success(true)
                .build();

        return ResponseEntity.ok(response);
    }

}

