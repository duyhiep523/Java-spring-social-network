package com.example.social_network.services.Iservice;

import com.example.social_network.comon.enums.FriendshipStatus;
import com.example.social_network.entities.Friendship;

public interface IFriendshipService {

    Friendship createFriendship(String userId1, String userId2);


    Friendship acceptFriendship(String userId1, String userId2);

    Friendship rejectFriendship(String userId1, String userId2);


    void deleteFriendship(String userId1, String userId2);

    FriendshipStatus getFriendshipStatus(String userId1, String userId2);
}
