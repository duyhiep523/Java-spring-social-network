package com.example.social_network.services.Iservice;

import com.example.social_network.dtos.Request.PrivateMessageDTO;
import com.example.social_network.dtos.Response.PrivateMessageHistoryResponse;
import com.example.social_network.dtos.Response.PrivateMessageResponse;
import com.example.social_network.entities.PrivateMessage;

import java.util.List;

public interface IPrivateMessageService {
    PrivateMessageResponse createMessage(PrivateMessageDTO request);

    PrivateMessageHistoryResponse getChatHistory(String senderId, String receiverId, int page, int size);

    void deleteMessage(String messageId, String userId);
}
