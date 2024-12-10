package com.example.social_network.dtos.Response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
    private String content;
    private String parentCommentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentResponse> childComments;
}
