package com.example.social_network.dtos.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HighlightStoryDetailResponse {
    @JsonProperty("story_id")
    private String storyId;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("story_name")
    private String storyName;
    @JsonProperty("create_at")
    private LocalDateTime createAt;
    private Boolean isDeleted;
   List<HighlightStoryImageResponse>  highlightStoryImageResponse;
}
