package com.example.social_network.dtos.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GroupChatResponse {
    private String groupId;
    private String groupName;
    private String adminId;
    private String image;
    private boolean isDelete;
}
