package com.example.social_network.dtos.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupMessageHistoryRespone {
    private String groupId;

    private List<GroupMessageHistoryRespone.GroupMessageResponse> messages;
    private Long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor

    public static class GroupMessageResponse {
        private String messageId;
        private GroupMessageHistoryRespone.SenderReceiverInfo sender;
        private String messageContent;
        private String messageType;
        private String attachment;
        private Boolean isDelete;
        private String createdAt;

    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SenderReceiverInfo {
        private String userId;
        private String fullName;
        private String avatarUrl;
    }


}
