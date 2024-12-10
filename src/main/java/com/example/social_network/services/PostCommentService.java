package com.example.social_network.services;

import com.example.social_network.dtos.Response.CommentResponse;
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
import java.util.stream.Collectors;

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
    public CommentResponse editComment(String commentId, String userId, String newContent) {

        PostComment postComment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Bình luận không tồn tại"));


        if (!postComment.getUserAccount().getUserId().equals(userId)) {
            throw new IllegalStateException("Bạn không có quyền sửa bình luận này");
        }


        postComment.setContent(newContent);
        postComment = postCommentRepository.save(postComment);


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
    public List<CommentResponse> getCommentsWithHierarchy(String postId) {
        List<PostComment> allComments = postCommentRepository.findByPost_PostIdOrderByCreatedAtAsc(postId);
        return buildCommentHierarchy(allComments, null);
    }

    private List<CommentResponse> buildCommentHierarchy(List<PostComment> allComments, String parentCommentId) {
        return allComments.stream()
                .filter(comment -> (comment.getParentComment() == null && parentCommentId == null) ||
                        (comment.getParentComment() != null && comment.getParentComment().getCommentId().equals(parentCommentId)))
                .map(comment -> {
                    CommentResponse response = CommentResponse.builder()
                            .commentId(comment.getCommentId())
                            .postId(comment.getPost().getPostId())
                            .userId(comment.getUserAccount().getUserId())
                            .content(comment.getContent())
                            .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getCommentId() : null)
                            .createdAt(comment.getCreatedAt())
                            .updatedAt(comment.getUpdatedAt())
                            .build();
                    List<CommentResponse> childComments = buildCommentHierarchy(allComments, comment.getCommentId());
                    response.setChildComments(childComments);
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public long countCommentsByPost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        return postCommentRepository.countByPost_PostId(postId);
    }

    @Override
    public void deleteComment(String commentId, String userId) {

        PostComment postComment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Bình luận không tồn tại"));

        if (!postComment.getUserAccount().getUserId().equals(userId)) {
            throw new IllegalStateException("Bạn không có quyền xóa bình luận này");
        }

        if (postCommentRepository.countByParentComment_CommentId(commentId) > 0) {
            throw new IllegalStateException("Không thể xóa bình luận này vì có bình luận con");
        }


        postCommentRepository.delete(postComment);
    }
}
