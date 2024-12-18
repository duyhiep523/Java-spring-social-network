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
public class GroupChatRequest {
    @JsonProperty("group_name")
    @NotBlank(message = "Tên group không được bỏ trống")
    private String groupName;
    @JsonProperty("admin_id")
    @NotBlank(message = "Admin không được bỏ trống")
    private String adminId;

    @JsonProperty("group_image_url")
    private MultipartFile image;
}
