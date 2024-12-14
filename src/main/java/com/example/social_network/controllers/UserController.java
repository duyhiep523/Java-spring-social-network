package com.example.social_network.controllers;

import com.example.social_network.dtos.Request.*;
import com.example.social_network.dtos.Response.FriendsListDTO;
import com.example.social_network.dtos.Response.UserResponse;
import com.example.social_network.dtos.Response.UserResponseLogin;
import com.example.social_network.dtos.Response.UserSearchResponse;
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

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<UserSearchResponse> users = iUserService.searchUsers(keyword, page, size);

            Response<Object> response = Response.builder()
                    .message("Tìm kiếm người dùng thành công")
                    .data(users)
                    .success(true)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Error errorResponse = new Error("Lỗi khi tìm kiếm người dùng: " + e.getMessage(), "500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UserLoginRequest loginRequest,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String error = errorMessages.toString();
            Error errorResponse = new Error(error, "422");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            UserResponseLogin loginResponse = iUserService.login(loginRequest);

            Response<Object> response = Response.builder()
                    .message("Đăng nhập thành công")
                    .data(loginResponse)
                    .success(true)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Error errorResponse = new Error("Đăng nhập thất bại: " + e.getMessage(), "401");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        UserResponse userResponse = iUserService.getUserById(userId);
        Response<Object> response = Response.builder()
                .message("Lấy thông tin người dùng thành công")
                .data(userResponse)
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<?> getFriends(@PathVariable String userId) {
        FriendsListDTO friendsListDTO = iUserService.getFriendsByUserId(userId);
        Response<Object> response = Response.builder()
                .message("Lấy danh sách bạn bè thành công")
                .data(friendsListDTO)
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }

}
