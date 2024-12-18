package com.example.social_network.dtos.Response;

import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class UserMessageSummaryResponse {
    private String userId;
    private String fullName;
    private String profilePictureUrl;
    private String lastMessageTime;

}