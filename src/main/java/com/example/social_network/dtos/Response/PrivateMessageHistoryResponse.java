package com.example.social_network.dtos.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class PrivateMessageHistoryResponse {
    private SenderReceiverInfo sender;
    private SenderReceiverInfo receiver;
    private List<PrivateMessageResponse> messages;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SenderReceiverInfo {
        private String userId;
        private String fullName;
        private String avatarUrl;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class PrivateMessageResponse {
        private String messageId;
        private String messageContent;
        private String messageType;
        private String sentAt;
        private boolean isSender;
    }
}
