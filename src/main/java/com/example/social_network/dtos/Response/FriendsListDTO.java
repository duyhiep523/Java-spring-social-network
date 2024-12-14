package com.example.social_network.dtos.Response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FriendsListDTO {
    private List<FriendDTO> friends;
}
