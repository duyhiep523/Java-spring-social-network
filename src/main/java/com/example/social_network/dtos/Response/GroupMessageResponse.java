package com.example.social_network.dtos.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class GroupMessageResponse {
    private String messageId;
    private GroupMessageResponse.SenderReceiverInfo sender;

    private String messageContent;
    private String messageType;
    private String attachment;
    private boolean isDelete;
    private String createdAt;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SenderReceiverInfo {
        private String userId;
        private String fullName;
        private String avatarUrl;
        private String nickName;
    }
}
