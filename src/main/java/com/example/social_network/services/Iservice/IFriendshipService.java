package com.example.social_network.services.Iservice;

import com.example.social_network.comon.enums.FriendshipStatus;
import com.example.social_network.dtos.Response.FriendReceivedResponse;
import com.example.social_network.dtos.Response.FriendRequestResponse;
import com.example.social_network.dtos.Response.FriendShipStatusUSent;
import com.example.social_network.dtos.Response.UserMutualFriendsResponse;
import com.example.social_network.entities.Friendship;

import java.util.List;

public interface IFriendshipService {

    Friendship createFriendship(String userId1, String userId2);


    Friendship acceptFriendship(String userId1, String userId2);

    Friendship blockFriendship(String userId1, String userId2);

    Friendship unBlockFriendship(String userId1, String userId2);


    void deleteFriendship(String userId1, String userId2);

    List<FriendRequestResponse> getReceivedFriendRequests(String receiverId);

    FriendShipStatusUSent getFriendshipStatus(String userId1, String userId2);

    long countFriends(String userId);

    List<FriendReceivedResponse> getSentFriendRequests(String senderId);

    List<UserMutualFriendsResponse> getUsersWithMutualFriends(String userId);
}
