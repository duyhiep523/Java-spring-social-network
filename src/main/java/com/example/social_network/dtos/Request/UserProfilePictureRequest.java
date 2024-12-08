package com.example.social_network.dtos.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfilePictureRequest {
    @NotNull(message = "Ảnh đại diện không được bỏ trống")
    private MultipartFile profilePicture;
}