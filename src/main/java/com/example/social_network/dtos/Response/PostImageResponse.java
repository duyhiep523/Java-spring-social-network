package com.example.social_network.dtos.Response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostImageResponse {
    private String imageId;
    private String imageUrl;
    private String postId;
    private String postContent;
    private String userId;
    private String userFullName;
    private String userAvatarUrl;
}
