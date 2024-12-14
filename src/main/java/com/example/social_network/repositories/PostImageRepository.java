package com.example.social_network.repositories;

import com.example.social_network.entities.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage,String> {
    List<PostImage> findByPost_PostId(String postId);
    @Modifying
    @Query(value = "DELETE FROM post_image WHERE post_id = :postId", nativeQuery = true)
    void deleteByPost_PostId(@Param("postId") String postId);
    @Query("SELECT pi FROM PostImage pi WHERE pi.post.userAccount.userId = :userId")
    List<PostImage> findAllImagesByUserId(@Param("userId") String userId);
}
