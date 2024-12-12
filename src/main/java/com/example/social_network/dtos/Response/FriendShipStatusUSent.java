package com.example.social_network.dtos.Response;

import com.example.social_network.comon.enums.FriendshipStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FriendShipStatusUSent {
    private FriendshipStatus status;
    private String uSent;
}
