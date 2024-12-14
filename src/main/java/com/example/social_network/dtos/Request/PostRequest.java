package com.example.social_network.dtos.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    @JsonProperty("user_id")
    @NotBlank(message = "user id null")
    private String userId;
    @JsonProperty("privacy")
    private String privacy;
    private String content;
    private String theme= null;
    private String share= null;

    @JsonProperty("images")
    private List<MultipartFile> images;
}
