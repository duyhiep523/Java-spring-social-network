package com.example.social_network.services;

import com.example.social_network.dtos.Response.CommentResponse;
import com.example.social_network.dtos.Response.PostResponse;
import com.example.social_network.entities.Post;
import com.example.social_network.entities.PostComment;
import com.example.social_network.entities.User;
import com.example.social_network.exceptions.ResourceNotFoundException;
import com.example.social_network.repositories.PostCommentRepository;
import com.example.social_network.repositories.PostRepository;
import com.example.social_network.repositories.UserAccountRepository;
import com.example.social_network.services.Iservice.IPostCommentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostCommentService implements IPostCommentService {
    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserAccountRepository userRepository;
    @Override
    @Transactional
    public CommentResponse addComment(String postId, String userId, String content, String parentCommentId) {
        // Tìm bài viết và người dùng
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        PostComment parentComment = null;
        if (parentCommentId != null) {
            parentComment = postCommentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found"));
        }


        PostComment postComment = new PostComment();
        postComment.setPost(post);
        postComment.setUserAccount(user);
        postComment.setContent(content);
        postComment.setParentComment(parentComment);
        postComment = postCommentRepository.save(postComment);

        // Trả về PostResponse trực tiếp sau khi lưu bình luận
        return CommentResponse.builder()
                .commentId(postComment.getCommentId())
                .postId(postComment.getPost().getPostId())
                .userId(postComment.getUserAccount().getUserId())
                .content(postComment.getContent())
                .parentCommentId(postComment.getParentComment() != null ? postComment.getParentComment().getCommentId() : null)
                .createdAt(postComment.getCreatedAt())
                .updatedAt(postComment.getUpdatedAt())
                .build();
    }
    @Override
    @Transactional
    public CommentResponse editComment(String commentId, String userId, String newContent) {
        // Tìm bình luận cần sửa
        PostComment postComment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        // Kiểm tra xem người dùng có quyền sửa bình luận hay không (chỉ có người tạo mới sửa được)
        if (!postComment.getUserAccount().getUserId().equals(userId)) {
            throw new IllegalStateException("You do not have permission to edit this comment");
        }

        // Cập nhật nội dung bình luận
        postComment.setContent(newContent);
        postComment = postCommentRepository.save(postComment);

        // Trả về thông tin bình luận đã sửa
        return CommentResponse.builder()
                .commentId(postComment.getCommentId())
                .postId(postComment.getPost().getPostId())
                .userId(postComment.getUserAccount().getUserId())
                .content(postComment.getContent())
                .parentCommentId(postComment.getParentComment() != null ? postComment.getParentComment().getCommentId() : null)
                .createdAt(postComment.getCreatedAt())
                .updatedAt(postComment.getUpdatedAt())
                .build();
    }

    // Lấy tất cả bình luận của một bài viết
    public List<PostComment> getCommentsByPost(String postId) {
        return postCommentRepository.findByPost_PostIdOrderByCreatedAtAsc(postId);
    }


}
