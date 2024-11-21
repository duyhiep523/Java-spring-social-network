package com.example.social_network.dtos.Response;




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
public class PostResponse {
    @JsonProperty("post_id")
    private String postId;
    private String content;
    private String privacy;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("create_at")
    private LocalDateTime createAt;

    @JsonProperty("images")
    private List<String> images;
}


