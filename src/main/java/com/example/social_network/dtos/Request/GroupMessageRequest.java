package com.example.social_network.dtos.Request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupMessageRequest {
    private String senderId;
    private String messageContent;
    private String messageType;
    private MultipartFile[] attachments;
}
