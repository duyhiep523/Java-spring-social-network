package com.example.social_network.services;

import com.example.social_network.comon.enums.FriendshipStatus;
import com.example.social_network.entities.Friendship;
import com.example.social_network.repositories.FriendshipRepository;
import com.example.social_network.repositories.UserAccountRepository;
import com.example.social_network.services.Iservice.IFriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
                return friendshipRepository.save(friendship);

            }
            throw new RuntimeException("Friendship already exists");
        }
        Friendship friendship = Friendship.builder()
                .user1(userRepository.findById(userId1).orElseThrow(() -> new RuntimeException("User not found")))
                .user2(userRepository.findById(userId2).orElseThrow(() -> new RuntimeException("User not found")))
                .status(FriendshipStatus.PENDING)
                .isDeleted(false)
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


    public Friendship rejectFriendship(String userId1, String userId2) {
        Optional<Friendship> friendshipOpt = friendshipRepository.findByUser1AndUser2AndIsDeletedFalse(userId1, userId2);

        if (friendshipOpt.isEmpty()) {
            throw new RuntimeException("Friendship request not found or already deleted");
        }
        Friendship friendship = friendshipOpt.get();
        friendship.setStatus(FriendshipStatus.REJECTED);
        return friendshipRepository.save(friendship);
    }

    public void deleteFriendship(String userId1, String userId2) {
        Optional<Friendship> friendshipOpt = friendshipRepository.findByUser1AndUser2AndIsDeletedFalse(userId1, userId2);
        if (friendshipOpt.isEmpty()) {
            throw new RuntimeException("Đã có mối quan hệ");
        }
        Friendship friendship = friendshipOpt.get();
        friendship.setDeleted(true);
        friendshipRepository.save(friendship);
    }

    public FriendshipStatus getFriendshipStatus(String userId1, String userId2) {
        Optional<Friendship> friendshipOpt = friendshipRepository.findByUser1AndUser2AndIsDeletedFalse(userId1, userId2);
        if (friendshipOpt.isEmpty()) {
            throw new RuntimeException("Friendship not found or already deleted");
        }
        return friendshipOpt.get().getStatus();
    }


}
