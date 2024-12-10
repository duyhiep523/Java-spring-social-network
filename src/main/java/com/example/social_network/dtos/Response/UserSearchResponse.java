package com.example.social_network.dtos.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSearchResponse {
    private String userId;
    private String username;
    private String fullName;
    private String profilePictureUrl;
}
