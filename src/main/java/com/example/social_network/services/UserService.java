package com.example.social_network.services;

import com.example.social_network.dtos.Request.UserBioUpdateRequest;
import com.example.social_network.dtos.Request.UserRegisterRequest;
import com.example.social_network.dtos.Response.UserResponse;
import com.example.social_network.entities.User;
import com.example.social_network.exceptions.ResourceNotFoundException;
import com.example.social_network.repositories.UserAccountRepository;
import com.example.social_network.services.Iservice.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserAccountRepository userRepository;
    @Autowired
    private com.example.social_network.services.CloudinaryService cloudinaryService;


    @Override
    public UserResponse register(UserRegisterRequest userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new ResourceNotFoundException("Người dùng đã tồn tại");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new ResourceNotFoundException("Email đã được đăng ký");
        }

        User user = User.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .fullName(userDTO.getFullName())
                .password(userDTO.getPassword())
                .gender(userDTO.getGender())
                .dateOfBirth(userDTO.getDateOfBirth())
                .isDeleted(false)
                .build();
        userRepository.save(user);

        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .password(user.getPassword())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .isDeleted(user.getIsDeleted())
                .build();
    }
    @Override
    public UserResponse updateBio(String userId, UserBioUpdateRequest bioRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        user.setBio(bioRequest.getBio());
        userRepository.save(user);

        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .bio(user.getBio())
                .build();
    }



    @Override
    public UserResponse updateProfilePicture(String userId, MultipartFile profilePicture) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        String profilePictureUrl = cloudinaryService.uploadImage(profilePicture);

        user.setProfilePictureUrl(profilePictureUrl);
        userRepository.save(user);

        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }


    @Override
    public UserResponse updateCoverPicture(String userId, MultipartFile coverPicture) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        String coverPictureUrl = cloudinaryService.uploadImage(coverPicture);

        user.setCoverPictureUrl(coverPictureUrl);
        userRepository.save(user);

        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .coverPictureUrl(user.getCoverPictureUrl())
                .build();
    }

    @Override
    public boolean existsByUserId(String userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public boolean existsByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

}
