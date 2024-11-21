package com.example.social_network.dtos.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HighlightStoryRequest {
    @JsonProperty("user_id")
    @NotBlank(message = "user id không được bỏ trống")
    private String userId;
    @JsonProperty("story_name")
    @NotBlank(message = "Tiêu đề tin nổi bật không được bỏ trống")
    private String storyName;
    @JsonProperty("images")
    private List<MultipartFile> images;
}
