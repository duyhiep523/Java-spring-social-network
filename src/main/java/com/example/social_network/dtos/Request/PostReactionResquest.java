package com.example.social_network.dtos.Request;

import com.example.social_network.comon.enums.PostReactionEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostReactionResquest {
    @JsonProperty("post_id")
    @NotBlank(message = "Post ID không được để trống.")
    private String postId;
    @JsonProperty("user_id")
    @NotBlank(message = "User ID không được để trống.")
    private String userId;
    @JsonProperty("reaction_type")
    private PostReactionEnum reactionType;

}
