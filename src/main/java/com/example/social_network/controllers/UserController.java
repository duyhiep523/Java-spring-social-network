package com.example.social_network.controllers;

import com.example.social_network.dtos.Request.UserBioUpdateRequest;
import com.example.social_network.dtos.Request.UserCoverPictureRequest;
import com.example.social_network.dtos.Request.UserProfilePictureRequest;
import com.example.social_network.dtos.Request.UserRegisterRequest;
import com.example.social_network.dtos.Response.UserResponse;
import com.example.social_network.response.Error;
import com.example.social_network.response.Response;
import com.example.social_network.services.Iservice.IUserService;
import com.example.social_network.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/users")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody UserRegisterRequest userDTO
            , BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String error = errorMessages.toString();
            Error errorResponse = new Error(error, "422");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        UserResponse registeredUser = iUserService.register(userDTO);
        Response<Object> response = Response.builder()
                .message("Đăng ký người dùng thành công")
                .data(registeredUser)
                .success(true)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{userId}/update-bio")
    public ResponseEntity<?> updateBio(
            @PathVariable String userId,
            @Valid @RequestBody UserBioUpdateRequest bioRequest,
            BindingResult result) {

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String error = errorMessages.toString();
            Error errorResponse = new Error(error, "422");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        UserResponse updatedUser = iUserService.updateBio(userId, bioRequest);

        Response<Object> response = Response.builder()
                .message("Cập nhật tiểu sử thành công")
                .data(updatedUser)
                .success(true)
                .build();

        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{userId}/update-profile-picture")
    public ResponseEntity<?> updateProfilePicture(
            @PathVariable String userId,
            @Valid @ModelAttribute UserProfilePictureRequest profileRequest,
            BindingResult result) {

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String error = errorMessages.toString();
            Error errorResponse = new Error(error, "422");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        UserResponse updatedUser = iUserService.updateProfilePicture(userId, profileRequest.getProfilePicture());

        Response<Object> response = Response.builder()
                .message("Cập nhật ảnh đại diện thành công")
                .data(updatedUser)
                .success(true)
                .build();

        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{userId}/update-cover-picture")
    public ResponseEntity<?> updateCoverPicture(
            @PathVariable String userId,
            @Valid @ModelAttribute UserCoverPictureRequest coverRequest,
            BindingResult result) {

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String error = errorMessages.toString();
            Error errorResponse = new Error(error, "422");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        UserResponse updatedUser = iUserService.updateCoverPicture(userId, coverRequest.getCoverPicture());

        Response<Object> response = Response.builder()
                .message("Cập nhật ảnh bìa thành công")
                .data(updatedUser)
                .success(true)
                .build();

        return ResponseEntity.ok(response);
    }


}
