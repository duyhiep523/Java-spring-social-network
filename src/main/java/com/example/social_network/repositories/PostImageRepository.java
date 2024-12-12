package com.example.social_network.repositories;

import com.example.social_network.entities.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage,String> {
    List<PostImage> findByPost_PostId(String postId);
}
