package com.example.social_network.services.Iservice;

import com.example.social_network.comon.enums.PostReactionEnum;
import com.example.social_network.dtos.Response.PostReactionResponse;
import com.example.social_network.dtos.Response.UserReactionResponse;
import com.example.social_network.entities.PostReaction;

import java.util.List;

public interface IPostReactionService {

    PostReactionResponse addReaction(String postId, String userId, PostReactionEnum reactionType);


    void deleteReaction(String postId, String userId);

    long countReactionsForPost(String postId);

    List<UserReactionResponse> getReactionsForPost(String postId);
}
