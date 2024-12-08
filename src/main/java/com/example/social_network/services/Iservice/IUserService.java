package com.example.social_network.services.Iservice;

import com.example.social_network.dtos.Request.UserBioUpdateRequest;
import com.example.social_network.dtos.Request.UserRegisterRequest;
import com.example.social_network.dtos.Response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {


    boolean existsByUserId(String userId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    UserResponse register(UserRegisterRequest userDTO);
    UserResponse updateBio(String userId, UserBioUpdateRequest bioRequest);
    UserResponse updateProfilePicture(String userId, MultipartFile profilePicture);
    UserResponse updateCoverPicture(String userId, MultipartFile coverPicture);
}
