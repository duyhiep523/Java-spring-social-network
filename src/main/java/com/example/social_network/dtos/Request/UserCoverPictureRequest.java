package com.example.social_network.dtos.Request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCoverPictureRequest {
    @NotNull(message = "Ảnh bìa không được bỏ trống")
    private MultipartFile coverPicture;
}
