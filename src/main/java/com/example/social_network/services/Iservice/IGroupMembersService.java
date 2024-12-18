package com.example.social_network.services.Iservice;

import com.example.social_network.dtos.Request.AddGroupMembersRequest;
import com.example.social_network.dtos.Response.GroupMemberResponse;

import java.util.List;

public interface IGroupMembersService {
    void addGroupMember(String groupId, String userId);


    void addGroupMembers(AddGroupMembersRequest request);

    void removeGroupMember(String adminId,String groupId, String userId);

    List<GroupMemberResponse> listGroupMembers(String groupId);
}
