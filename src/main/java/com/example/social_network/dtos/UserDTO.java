package com.example.social_network.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;

@Getter
@Setter
public class UserDTO {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 5, max = 100)
    private String email;

    @NotBlank
    @Size(min = 3, max = 100)
    private String fullName;

    @Size(max = 255)
    private String bio;

    @Size(max = 255)
    private String hometown;

    private LocalDateTime dateOfBirth;

    private MultipartFile profilePictureUrl;
    private MultipartFile coverPictureUrl;
}
