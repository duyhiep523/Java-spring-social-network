package com.example.social_network.dtos.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HighlightStoryResponse {
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("story_name")
    private String storyName;
    @JsonProperty("create_at")
    private LocalDateTime createAt;
    @JsonProperty("images")
    private List<String> images;
}
