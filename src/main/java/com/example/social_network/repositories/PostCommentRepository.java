package com.example.social_network.repositories;

import com.example.social_network.entities.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, String> {
    List<PostComment> findByPost_PostIdOrderByCreatedAtAsc(String postId);

    long countByPost_PostId(String postId);
    long countByParentComment_CommentId(String parentCommentId);
}
