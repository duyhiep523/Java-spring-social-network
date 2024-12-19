package com.example.social_network.dtos.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
public class FriendReceivedResponse {
    private String receivedId;
    private String fullName;
    private String profilePictureUrl;
    private LocalDateTime sentAt;
}
