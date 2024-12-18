package com.example.social_network.dtos.Request;


import lombok.Data;

import java.util.List;

@Data
public class AddGroupMembersRequest {
    private String groupId;
    private List<String> userIds;
}