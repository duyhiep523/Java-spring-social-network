package com.example.social_network.repositories;

import com.example.social_network.entities.GroupChat;
import com.example.social_network.entities.GroupMembers;
import com.example.social_network.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMembersRepository extends JpaRepository<GroupMembers, String> {
    Optional<GroupMembers> findByGroupChat_GroupIdAndUserAccount_UserId(String groupId, String userId);

    List<GroupMembers> findByGroupChat(GroupChat groupChat);
    boolean existsByGroupChatAndUserAccount(GroupChat groupChat, User user);

}
