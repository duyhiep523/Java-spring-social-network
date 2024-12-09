package com.example.social_network.dtos.Request;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PrivateMessageDTO {
    private String senderId;
    private String receiverId;
    private String messageContent;
    private String messageType;
    private MultipartFile[] attachments;
}