package com.example.social_network.repositories;

import com.example.social_network.entities.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostReactionRepository extends JpaRepository<PostReaction, String> {


    Optional<PostReaction> findByPost_PostIdAndUserAccount_UserId(String postId, String userId);
    List<PostReaction> findByPost_PostId(String postId);
    long countByPost_PostId(String postId);
}
