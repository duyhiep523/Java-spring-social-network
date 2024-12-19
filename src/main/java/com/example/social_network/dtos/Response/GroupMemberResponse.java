package com.example.social_network.dtos.Response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GroupMemberResponse {
    private String userId;
    private String avatarUrl;
    private String fullName;
    private String nickName;
}
