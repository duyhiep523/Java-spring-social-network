package com.example.social_network.repositories;

import com.example.social_network.dtos.Response.UserMessageSummaryResponse;
import com.example.social_network.entities.PrivateMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface PrivateMessageRepository extends JpaRepository<PrivateMessage,String> {
    @Query(value = "SELECT * FROM private_message pm WHERE (pm.sender_id = :senderId AND pm.receiver_id = :receiverId) OR (pm.sender_id = :receiverId AND pm.receiver_id = :senderId) ORDER BY pm.create_at DESC",
            countQuery = "SELECT COUNT(*) FROM private_message pm WHERE (pm.sender_id = :senderId AND pm.receiver_id = :receiverId) OR (pm.sender_id = :receiverId AND pm.receiver_id = :senderId)",
            nativeQuery = true)
    Page<PrivateMessage> findPrivateMessages(@Param("senderId") String senderId,
                                             @Param("receiverId") String receiverId,
                                             Pageable pageable);


    @Query(value = "SELECT u.user_id, u.full_name, u.profile_picture_url, last_messages.last_message_time " +
            "FROM user_account u " +
            "JOIN ( " +
            "    SELECT CASE " +
            "        WHEN sender_id = :userId THEN receiver_id " +
            "        WHEN receiver_id = :userId THEN sender_id " +
            "    END AS other_user_id, " +
            "    MAX(pm.create_at) AS last_message_time " +
            "    FROM private_message pm " +
            "    WHERE (pm.sender_id = :userId OR pm.receiver_id = :userId) " +
            "    AND pm.is_deleted = 0 " +
            "    GROUP BY other_user_id " +
            ") AS last_messages " +
            "ON u.user_id = last_messages.other_user_id " +
            "ORDER BY last_messages.last_message_time DESC", nativeQuery = true)
    List<Object[]> getUserMessageSummaries(@Param("userId") String userId);
}

