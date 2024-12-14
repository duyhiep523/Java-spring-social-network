package com.example.social_network.services.Iservice;

import com.example.social_network.dtos.Request.UserBioUpdateRequest;
import com.example.social_network.dtos.Request.UserLoginRequest;
import com.example.social_network.dtos.Request.UserRegisterRequest;
import com.example.social_network.dtos.Response.FriendsListDTO;
import com.example.social_network.dtos.Response.UserResponse;
import com.example.social_network.dtos.Response.UserResponseLogin;
import com.example.social_network.dtos.Response.UserSearchResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {
    UserResponseLogin login(UserLoginRequest loginRequest);

    List<UserSearchResponse> searchUsers(String keyword, int page, int size);

    UserResponse getUserById(String userId);

    FriendsListDTO getFriendsByUserId(String userId);

    boolean existsByUserId(String userId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    UserResponse register(UserRegisterRequest userDTO);
    UserResponse updateBio(String userId, UserBioUpdateRequest bioRequest);
    UserResponse updateProfilePicture(String userId, MultipartFile profilePicture);
    UserResponse updateCoverPicture(String userId, MultipartFile coverPicture);
}
