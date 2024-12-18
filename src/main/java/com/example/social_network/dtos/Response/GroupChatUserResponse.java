package com.example.social_network.dtos.Response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GroupChatUserResponse {
    private String groupId;
    private String groupName;
    private String groupImageUrl;
    private LastMessageResponse lastMessageResponse;


}
