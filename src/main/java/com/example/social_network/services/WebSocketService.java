package com.example.social_network.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void pushMessage(String message, String receiverId) {
        messagingTemplate.convertAndSendToUser(receiverId, "/topic", message);
    }
}
