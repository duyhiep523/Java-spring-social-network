package com.example.social_network.services.Iservice;

import com.example.social_network.dtos.Request.PostRequest;
import com.example.social_network.dtos.Response.PostResponse;
import com.example.social_network.entities.Post;

import java.util.List;

public interface IPostService {
    PostResponse createPost(PostRequest postCreateRequest);

    Post updatePost(Long postId, PostRequest postUpdateRequest);

    void deletePost(Long postId);

    PostResponse getPostById(Long postId);

    List<PostResponse> getPostsByUserId(Long userId);

    List<PostResponse> getAllPosts(int page, int size);

}
