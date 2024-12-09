package com.example.social_network.repositories;

import com.example.social_network.entities.PrivateMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface PrivateMessageRepository extends JpaRepository<PrivateMessage,String> {
    @Query("SELECT pm FROM PrivateMessage pm WHERE pm.sender.userId = :senderId AND pm.receiver.userId = :receiverId")
    Page<PrivateMessage> findPrivateMessages(String senderId, String receiverId, Pageable pageable);

}

