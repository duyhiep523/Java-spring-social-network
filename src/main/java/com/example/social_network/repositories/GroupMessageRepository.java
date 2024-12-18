package com.example.social_network.repositories;

import com.example.social_network.entities.GroupChat;
import com.example.social_network.entities.GroupMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupMessageRepository extends JpaRepository<GroupMessage,String> {
    Page<GroupMessage> findByGroupChat(GroupChat groupChat, Pageable pageable);

    GroupMessage findTopByGroupChatOrderByCreatedAtDesc(GroupChat groupChat);

    @Query(value = """
    SELECT m.message_id, m.sender_id, ua.full_name, m.message_content, m.create_at
    FROM group_message m
    JOIN user_account ua ON m.sender_id = ua.user_id
    WHERE m.group_id = :groupId 
      AND m.is_deleted = FALSE
    ORDER BY m.create_at DESC
    LIMIT 1
    """, nativeQuery = true)
    Optional<Object[]> findLastMessageByGroupId(@Param("groupId") String groupId);




}
