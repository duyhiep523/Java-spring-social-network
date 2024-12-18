package com.example.social_network.repositories;

import com.example.social_network.entities.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat,String> {
    Optional<GroupChat> findByGroupIdAndAdmin_UserId(String groupId, String userId);
}
