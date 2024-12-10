package com.example.social_network.services.Iservice;

import com.example.social_network.dtos.Response.CommentResponse;

import java.util.List;

public interface IPostCommentService {

    CommentResponse addComment(String postId, String userId, String content, String parentCommentId);
    CommentResponse editComment(String commentId, String userId, String newContent);

    List<CommentResponse> getCommentsWithHierarchy(String postId);

    long countCommentsByPost(String postId);

    void deleteComment(String commentId, String userId);
}
