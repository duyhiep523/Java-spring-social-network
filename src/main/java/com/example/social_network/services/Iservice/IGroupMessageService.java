package com.example.social_network.services.Iservice;

import com.example.social_network.dtos.Request.GroupMessageRequest;
import com.example.social_network.dtos.Response.GroupMessageResponse;

public interface IGroupMessageService {
    GroupMessageResponse createMessage(String groupId, GroupMessageRequest request);
}
