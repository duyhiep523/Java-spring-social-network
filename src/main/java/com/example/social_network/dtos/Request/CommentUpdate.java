package com.example.social_network.dtos.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CommentUpdate {

    @NotNull(message = "Post ID không được để trống.")
    private String postId;

    @NotNull(message = "User ID không được để trống.")
    private String userId;

    @NotBlank(message = "Nội dung bình luận không được để trống.")
    private String newContent;
}
