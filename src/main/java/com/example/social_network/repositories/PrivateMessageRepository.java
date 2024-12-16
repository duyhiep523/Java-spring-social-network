package com.example.social_network.repositories;

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



}

