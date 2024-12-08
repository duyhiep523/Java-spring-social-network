package com.example.social_network.dtos.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class UserLoginRequest {

    @JsonProperty("phone_number")
    @NotBlank(message = "Số điện thoại không được bỏ trống")
    private String phoneNumber;

    @NotBlank(message = "Mật khẩu không được bỏ trống")
    private String password;
}
