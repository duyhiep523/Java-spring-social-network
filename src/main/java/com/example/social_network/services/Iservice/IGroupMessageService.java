package com.example.social_network.services.Iservice;

import com.example.social_network.dtos.Request.GroupMessageRequest;
import com.example.social_network.dtos.Response.GroupChatUserResponse;
import com.example.social_network.dtos.Response.GroupMessageHistoryRespone;
import com.example.social_network.dtos.Response.GroupMessageResponse;
import com.example.social_network.dtos.Response.LastMessageResponse;

import java.util.List;

public interface IGroupMessageService {
    GroupMessageResponse createMessage(String groupId, GroupMessageRequest request);

    void deleteMessage(String messageId, String userId);

    GroupMessageHistoryRespone getGroupMessageHistory(String groupId, int page, int size);


    List<GroupChatUserResponse> getGroupChatsByUser(String userId);

    LastMessageResponse getLastMessageByGroupId(String groupId);
}
