package com.example.social_network.services.Iservice;

import com.example.social_network.dtos.Request.PrivateMessageDTO;
import com.example.social_network.dtos.Response.PrivateMessageResponse;
import com.example.social_network.entities.PrivateMessage;

import java.util.List;

public interface IPrivateMessageService {
    PrivateMessageResponse createMessage(PrivateMessageDTO request);

    List<PrivateMessageResponse> getChatHistory(String senderId, String receiverId, int page, int size);
}
