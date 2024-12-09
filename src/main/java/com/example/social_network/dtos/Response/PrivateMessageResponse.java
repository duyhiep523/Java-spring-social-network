package com.example.social_network.dtos.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PrivateMessageResponse {

    private String messageId;
    private SenderReceiverInfo sender;
    private SenderReceiverInfo receiver;
    private String messageContent;
    private String messageType;
    private String sentAt;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SenderReceiverInfo {
        private String userId;
        private String fullName;
        private String avatarUrl;
    }
}
