package com.example.social_network.repositories;

import com.example.social_network.comon.enums.FriendshipStatus;
import com.example.social_network.dtos.Response.UserMutualFriendsResponse;
import com.example.social_network.entities.Friendship;
import com.example.social_network.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query("SELECT COUNT(f) FROM Friendship f WHERE (f.user1.userId = :userId OR f.user2.userId = :userId) AND f.status = :status AND f.isDeleted = false")
    long countFriendsByUserIdAndStatus(@Param("userId") String userId, @Param("status") FriendshipStatus status);


    @Query("SELECT f FROM Friendship f WHERE (f.user1.id = :userId OR f.user2.id = :userId) AND f.status = :status")
    List<Friendship> findByUserIdAndStatus(@Param("userId") String userId, @Param("status") FriendshipStatus  status);
    List<Friendship> findAllByUser1_UserIdAndStatusAndIsDeletedFalse(String senderId, FriendshipStatus status);


    @Query(value = """
            SELECT ua.user_id AS userId, ua.full_name AS fullName, 
                   ua.profile_picture_url AS profilePictureUrl, 
                   COUNT(f2.user_id_2) AS mutualFriends 
            FROM user_account ua 
            LEFT JOIN friendship f1 ON f1.user_id_2 = ua.user_id AND f1.status = 'ACCEPTED' 
            LEFT JOIN friendship f2 ON f2.user_id_1 = f1.user_id_1 AND f2.user_id_2 != :userId AND f2.status = 'ACCEPTED' 
            WHERE ua.user_id != :userId 
              AND ua.user_id NOT IN (
                  SELECT CASE 
                             WHEN f.user_id_1 = :userId THEN f.user_id_2 
                             WHEN f.user_id_2 = :userId THEN f.user_id_1 
                         END 
                  FROM friendship f 
                  WHERE (f.user_id_1 = :userId OR f.user_id_2 = :userId) 
                    AND f.status = 'ACCEPTED' 
              ) 
            GROUP BY ua.user_id 
            ORDER BY mutualFriends DESC 
            """, nativeQuery = true)
    List<Object[]> findUsersWithMutualFriends(@Param("userId") String userId);

}
