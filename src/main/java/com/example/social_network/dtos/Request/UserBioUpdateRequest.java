package com.example.social_network.dtos.Request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBioUpdateRequest {
    @Size(max = 255, message = "Tiểu sử không được vượt quá 255 ký tự")
    private String bio;
}