package com.example.social_network.dtos.Response;

import com.example.social_network.comon.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserResponse {
    private String userId;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String bio;
    private String profilePictureUrl;
    private String coverPictureUrl;
    private Gender gender;
    private String hometown;
    private LocalDate dateOfBirth;
    private Boolean isDeleted;
}
