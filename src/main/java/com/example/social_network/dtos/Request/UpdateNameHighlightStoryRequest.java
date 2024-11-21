package com.example.social_network.dtos.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNameHighlightStoryRequest {
    @JsonProperty("story_name")
    @NotBlank(message = "Tiêu đề tin nổi bật không được bỏ trống")
    private String storyName;
}
