package com.example.social_network.repositories;

import com.example.social_network.comon.enums.FriendshipStatus;
import com.example.social_network.entities.Friendship;
import com.example.social_network.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship,String> {
    Optional<Friendship> findByUser1AndUser2(User userId1, User userId2);

    @Query("SELECT f FROM Friendship f WHERE f.user1.userId = :userId1 AND f.user2.userId = :userId2 AND f.isDeleted = false")
    Optional<Friendship> findByUser1AndUser2AndIsDeletedFalse(String userId1, String userId2);
    @Query("SELECT f FROM Friendship f WHERE " +
            "(f.user1.id = :userId1 AND f.user2.id = :userId2) OR " +
            "(f.user1.id = :userId2 AND f.user2.id = :userId1)")
    Optional<Friendship> findByUsers(String userId1, String userId2);

    List<Friendship> findAllByUser2_UserIdAndStatusAndIsDeletedFalse(String userId2, FriendshipStatus status);



}
