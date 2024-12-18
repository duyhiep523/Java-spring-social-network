package com.example.social_network.services.Iservice;

import com.example.social_network.dtos.Request.GroupChatRequest;
import com.example.social_network.dtos.Response.GroupChatResponse;

public interface IGroupChatService {
    GroupChatResponse createGroupChat(GroupChatRequest request);

    GroupChatResponse updateGroupChat(String groupId, GroupChatRequest request);
}
