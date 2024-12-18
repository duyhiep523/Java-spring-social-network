package com.example.social_network.dtos.Response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LastMessageResponse {
    private String messageId;
    private String senderId;
    private String senderName;
    private String messageContent;
    private String createAt;
}
