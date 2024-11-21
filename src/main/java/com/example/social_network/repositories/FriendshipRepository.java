package com.example.social_network.repositories;

import com.example.social_network.comon.enums.FriendshipStatus;
import com.example.social_network.entities.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship,String> {
    Optional<Friendship> findByUser1AndUser2(String userId1, String userId2);

    Optional<Friendship> findByUser1AndUser2AndIsDeletedFalse(String userId1, String userId2);


    List<Friendship> findByUser1AndStatusAndIsDeletedFalse(String userId, FriendshipStatus status);

    List<Friendship> findByUser2AndStatusAndIsDeletedFalse(String userId, FriendshipStatus status);
}
