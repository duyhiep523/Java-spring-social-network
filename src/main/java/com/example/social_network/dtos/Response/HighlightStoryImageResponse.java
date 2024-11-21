package com.example.social_network.dtos.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HighlightStoryImageResponse {
    @JsonProperty("story_id")
    private String storyId;
    private String imageId;
    private String imageUrl;
    private Boolean isDeleted;
}
