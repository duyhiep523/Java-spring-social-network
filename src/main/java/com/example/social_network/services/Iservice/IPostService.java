package com.example.social_network.services.Iservice;

import com.example.social_network.dtos.Request.PostRequest;
import com.example.social_network.dtos.Response.PostResponse;
import com.example.social_network.dtos.Response.PostResponseDetail;
import com.example.social_network.entities.Post;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IPostService {
    PostResponse createPost(PostRequest postCreateRequest);

    @Transactional(readOnly = true)
    PostResponseDetail getPostById(String postId);

    @Transactional
    void deletePost(String postId);

    PostResponse updatePost(String postId, PostRequest postUpdateRequest);

    PostResponse getPostById(Long postId);

    List<PostResponse> getPostsByUserId(Long userId);

    List<PostResponse> getAllPosts(int page, int size);

}
