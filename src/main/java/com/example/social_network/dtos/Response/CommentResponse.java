package com.example.social_network.dtos.Response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CommentResponse {
    private String commentId;
    private String postId;
    private String userId;
    private String userName;
    private String content;
    private String parentCommentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
