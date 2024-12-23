package com.example.social_network.services;

import com.example.social_network.comon.enums.FriendshipStatus;
import com.example.social_network.dtos.Response.FriendReceivedResponse;
import com.example.social_network.dtos.Response.FriendRequestResponse;
import com.example.social_network.dtos.Response.FriendShipStatusUSent;
import com.example.social_network.dtos.Response.UserMutualFriendsResponse;
import com.example.social_network.entities.Friendship;
import com.example.social_network.exceptions.ResourceNotFoundException;
import com.example.social_network.repositories.FriendshipRepository;
import com.example.social_network.repositories.UserAccountRepository;
import com.example.social_network.services.Iservice.IFriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendshipService implements IFriendshipService {
    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private UserAccountRepository userRepository;

    public Friendship createFriendship(String userId1, String userId2) {
        Optional<Friendship> existingFriendship = friendshipRepository.findByUsers(userId1, userId2);

        if (existingFriendship.isPresent()) {
            Friendship friendship = existingFriendship.get();
            if (friendship.isDeleted()) {
                friendship.setDeleted(false);
                friendship.setStatus(FriendshipStatus.PENDING);
                friendship.setUSent(userId1);
                return friendshipRepository.save(friendship);

            }
            throw new RuntimeException("Friendship already exists");
        }
        Friendship friendship = Friendship.builder()
                .user1(userRepository.findById(userId1).orElseThrow(() -> new RuntimeException("User not found")))
                .user2(userRepository.findById(userId2).orElseThrow(() -> new RuntimeException("User not found")))
                .status(FriendshipStatus.PENDING)
                .isDeleted(false)
                .uSent(userId1)
                .build();
        return friendshipRepository.save(friendship);
    }

    public Friendship acceptFriendship(String userId1, String userId2) {
        Optional<Friendship> friendshipOpt = friendshipRepository.findByUser1AndUser2AndIsDeletedFalse(userId1, userId2);

        if (friendshipOpt.isEmpty()) {
            throw new RuntimeException("Yêu cầu kết bạn không tìm thấy hoặc bị xoá");
        }
        Friendship friendship = friendshipOpt.get();
        if (friendship.getStatus() == FriendshipStatus.ACCEPTED) {
            throw new RuntimeException("Hai người đã là bạn bè");
        }

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        return friendshipRepository.save(friendship);
    }


    public Friendship blockFriendship(String userId1, String userId2) {
        Optional<Friendship> friendshipOpt = friendshipRepository.findByUser1AndUser2AndIsDeletedFalse(userId1, userId2);

        if (friendshipOpt.isEmpty()) {
            throw new RuntimeException("Friendship request not found or already deleted");
        }
        Friendship friendship = friendshipOpt.get();
        friendship.setStatus(FriendshipStatus.BLOCK);
        return friendshipRepository.save(friendship);
    }

    public Friendship unBlockFriendship(String userId1, String userId2) {
        Optional<Friendship> friendshipOpt = friendshipRepository.findByUser1AndUser2AndIsDeletedFalse(userId1, userId2);

        if (friendshipOpt.isEmpty()) {
            throw new RuntimeException("Friendship request not found or already deleted");
        }
        Friendship friendship = friendshipOpt.get();
        friendship.setStatus(null);
        friendship.setDeleted(true);
        return friendshipRepository.save(friendship);
    }

    public void deleteFriendship(String userId1, String userId2) {
        Optional<Friendship> friendshipOpt = friendshipRepository.findByUser1AndUser2AndIsDeletedFalse(userId1, userId2);
        if (friendshipOpt.isEmpty()) {
            throw new RuntimeException("Không có mối quan hệ bạn bè");
        }
        Friendship friendship = friendshipOpt.get();
        friendship.setDeleted(true);
        friendshipRepository.save(friendship);
    }

    @Override
    public List<FriendRequestResponse> getReceivedFriendRequests(String receiverId) {
        List<Friendship> friendRequests = friendshipRepository
                .findAllByUser2_UserIdAndStatusAndIsDeletedFalse(receiverId, FriendshipStatus.PENDING);

        return friendRequests.stream()
                .map(request -> FriendRequestResponse.builder()
                        .senderId(request.getUser1().getUserId())
                        .fullName(request.getUser1().getFullName())
                        .profilePictureUrl(request.getUser1().getProfilePictureUrl())
                        .sentAt(request.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
@Override
    public FriendShipStatusUSent getFriendshipStatus(String userId1, String userId2) {
        Optional<Friendship> friendshipOpt = friendshipRepository.findByUser1AndUser2AndIsDeletedFalse(userId1, userId2);
        Optional<Friendship> friendshipOpt2 = friendshipRepository.findByUser1AndUser2AndIsDeletedFalse(userId2, userId1);

        if (friendshipOpt.isEmpty()) {
            if (friendshipOpt2.isEmpty()) {
                throw new RuntimeException("Friendship not found or already deleted");
            }
            return FriendShipStatusUSent.builder()
                    .status(friendshipOpt2.get().getStatus())
                    .uSent(friendshipOpt2.get().getUSent())
                    .build();
        } else
                return FriendShipStatusUSent.builder()
                    .status(friendshipOpt.get().getStatus())
                    .uSent(friendshipOpt.get().getUSent())
                    .build();
    }

    @Override
    public long countFriends(String userId) {
        userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));
        return friendshipRepository.countFriendsByUserIdAndStatus(userId, FriendshipStatus.ACCEPTED);
    }
    @Override
    public List<FriendReceivedResponse> getSentFriendRequests(String senderId) {
        List<Friendship> sentRequests = friendshipRepository
                .findAllByUser1_UserIdAndStatusAndIsDeletedFalse(senderId, FriendshipStatus.PENDING);

        return sentRequests.stream()
                .map(request -> FriendReceivedResponse.builder()
                        .receivedId(request.getUser2().getUserId())
                        .fullName(request.getUser2().getFullName())
                        .profilePictureUrl(request.getUser2().getProfilePictureUrl())
                        .sentAt(request.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<UserMutualFriendsResponse> getUsersWithMutualFriends(String userId) {
        List<Object[]> results = friendshipRepository.findUsersWithMutualFriends(userId);
        List<UserMutualFriendsResponse> response = new ArrayList<>();

        for (Object[] row : results) {
            String userIdResult = (String) row[0];
            String fullName = (String) row[1];
            String profilePictureUrl = (String) row[2];
            long mutualFriends = ((Number) row[3]).longValue(); // Vì COUNT trả về là Number

            UserMutualFriendsResponse dto = new UserMutualFriendsResponse(userIdResult, fullName, profilePictureUrl, mutualFriends);
            response.add(dto);
        }
        return response;
    }
}
