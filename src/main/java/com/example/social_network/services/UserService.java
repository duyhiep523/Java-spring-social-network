package com.example.social_network.services;

import com.example.social_network.comon.enums.FriendshipStatus;
import com.example.social_network.dtos.Request.UserBioUpdateRequest;
import com.example.social_network.dtos.Request.UserLoginRequest;
import com.example.social_network.dtos.Request.UserRegisterRequest;
import com.example.social_network.dtos.Response.*;
import com.example.social_network.entities.Friendship;
import com.example.social_network.entities.User;
import com.example.social_network.exceptions.ResourceNotFoundException;
import com.example.social_network.repositories.FriendshipRepository;
import com.example.social_network.repositories.UserAccountRepository;
import com.example.social_network.services.Iservice.IUserService;
import com.example.social_network.utils.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserAccountRepository userRepository;
    @Autowired
    private com.example.social_network.services.CloudinaryService cloudinaryService;
    @Autowired
    private FriendshipRepository friendshipRepository;

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
                .profilePictureUrl(user.getProfilePictureUrl())
                .coverPictureUrl(user.getCoverPictureUrl())
                .gender(user.getGender())
                .hometown(user.getHometown())
                .dateOfBirth(user.getDateOfBirth())
                .isDeleted(user.getIsDeleted())
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
                .bio(user.getBio())
                .profilePictureUrl(user.getProfilePictureUrl())
                .coverPictureUrl(user.getCoverPictureUrl())
                .gender(user.getGender())
                .hometown(user.getHometown())
                .dateOfBirth(user.getDateOfBirth())
                .isDeleted(user.getIsDeleted())
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
                .bio(user.getBio())
                .profilePictureUrl(user.getProfilePictureUrl())
                .coverPictureUrl(user.getCoverPictureUrl())
                .gender(user.getGender())
                .hometown(user.getHometown())
                .dateOfBirth(user.getDateOfBirth())
                .isDeleted(user.getIsDeleted())
                .build();
    }

    @Override
    public UserResponseLogin login(UserLoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getPhoneNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        if (!loginRequest.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Mật khẩu không chính xác");
        }

        return UserResponseLogin.builder()
                .id(user.getUserId())
                .build();
    }

    @Override
    public List<UserSearchResponse> searchUsers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> result = userRepository.findAll(UserSpecification.containsKeyword(keyword), pageable);

        return result.stream()
                .map(user -> new UserSearchResponse(
                        user.getUserId(),
                        user.getUsername(),
                        user.getFullName(),
                        user.getProfilePictureUrl()  // Nếu muốn trả về hình đại diện
                ))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .bio(user.getBio())
                .profilePictureUrl(user.getProfilePictureUrl())
                .coverPictureUrl(user.getCoverPictureUrl())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .isDeleted(user.getIsDeleted())
                .build();
    }

    @Override
    public FriendsListDTO getFriendsByUserId(String userId) {
        List<Friendship> friendships = friendshipRepository.findByUserIdAndStatus(userId, FriendshipStatus.ACCEPTED);

        List<FriendDTO> friends = friendships.stream()
                .map(friendship -> {
                    String friendId = (friendship.getUser1().getUserId().equals(userId)) ? friendship.getUser2().getUserId() : friendship.getUser1().getUserId();
                    User friendUser = userRepository.findById(friendId).orElse(null);
                    if (friendUser != null) {
                        return new FriendDTO(
                                friendUser.getUserId(),
                                friendUser.getFullName(),
                                friendUser.getEmail(),
                                friendUser.getProfilePictureUrl()
                        );
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new FriendsListDTO(friends);
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
