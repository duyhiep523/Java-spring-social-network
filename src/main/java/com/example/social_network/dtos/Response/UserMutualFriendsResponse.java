package com.example.social_network.dtos.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMutualFriendsResponse {
    private String userId;
    private String fullName;
    private String profilePictureUrl;
    private long mutualFriends;
}
