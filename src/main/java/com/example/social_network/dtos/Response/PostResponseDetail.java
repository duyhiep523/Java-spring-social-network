package com.example.social_network.dtos.Response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PostResponseDetail {
    private PostResponse postResponse;
    private long commentCount;
    private long reactionCount;
}
