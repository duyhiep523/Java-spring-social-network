package com.example.social_network.dtos.Request;

import com.example.social_network.comon.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    @NotBlank(message = "Số điện thoại không được bỏ trống")
    private String username;
    @NotBlank(message = "Email không được bỏ trống")
    private String email;
    @NotBlank(message = "Mật khẩu không được bỏ trống")
    private String password;
    @NotBlank(message = "Họ và tên không được bỏ trống")
    private String fullName;
    @NotNull(message = "Giới tính không được bỏ trống")
    private Gender gender;
    @JsonProperty("date_of_birth")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "Ngày tháng năm sinh thiếu")
    private LocalDate dateOfBirth;
}
