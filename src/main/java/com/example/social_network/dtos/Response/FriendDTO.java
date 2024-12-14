package com.example.social_network.dtos.Response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FriendDTO {
    private String id;
    private String fullName;
    private String email;
    private String avatarUrl;
}
