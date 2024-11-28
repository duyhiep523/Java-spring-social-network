package com.example.social_network.dtos.Response;

import com.example.social_network.comon.enums.PostReactionEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostReactionResponse {
    private String reactionId;
    private String postId;
    private String userId;
    private PostReactionEnum reactionType;
    private Boolean isDeleted;

}
