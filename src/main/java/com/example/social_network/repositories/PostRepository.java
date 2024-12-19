package com.example.social_network.repositories;

import com.example.social_network.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,String> {
    @Query(value = "SELECT DISTINCT p.* FROM post p " +
            "JOIN friendship f ON (f.user_id_1 = p.user_id OR f.user_id_2 = p.user_id) " +
            "WHERE (f.user_id_1 = :userId OR f.user_id_2 = :userId) " +
            "AND f.status = 'ACCEPTED' AND p.is_deleted = FALSE " +
            "ORDER BY p.create_at DESC",
            countQuery = "SELECT COUNT(*) FROM post p " +
                    "JOIN friendship f ON (f.user_id_1 = p.user_id OR f.user_id_2 = p.user_id) " +
                    "WHERE (f.user_id_1 = :userId OR f.user_id_2 = :userId) " +
                    "AND f.status = 'ACCEPTED' AND p.is_deleted = FALSE",
            nativeQuery = true)
    Page<Post> findAllPostsByFriendship(@Param("userId") String userId, Pageable pageable);


    @Query("SELECT p FROM Post p WHERE p.userAccount.userId = :userId AND p.isDeleted = false ORDER BY p.createdAt DESC")
    Page<Post> findPostsByUserId(@Param("userId") String userId, Pageable pageable);
}
