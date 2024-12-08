package com.example.social_network.services.Iservice;

import com.example.social_network.dtos.Response.CommentResponse;

public interface IPostCommentService {

    CommentResponse addComment(String postId, String userId, String content, String parentCommentId);
    CommentResponse editComment(String commentId, String userId, String newContent);
}
