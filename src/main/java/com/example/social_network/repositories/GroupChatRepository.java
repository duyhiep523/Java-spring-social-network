package com.example.social_network.repositories;

import com.example.social_network.dtos.Response.GroupChatResponse;
import com.example.social_network.dtos.Response.GroupChatUserResponse;
import com.example.social_network.entities.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat,String> {
    Optional<GroupChat> findByGroupIdAndAdmin_UserId(String groupId, String userId);





    @Query(value = "SELECT g.group_id, g.group_name, g.group_image_url " +
            "FROM group_chat g " +
            "JOIN group_members gm ON g.group_id = gm.group_id " +
            "LEFT JOIN group_message m ON g.group_id = m.group_id " +
            "WHERE gm.user_id = :userId " +
            "AND gm.is_deleted = FALSE " +
            "AND g.is_deleted = FALSE " +
            "GROUP BY g.group_id, g.group_name, g.group_image_url " +
            "ORDER BY MAX(m.create_at) DESC", nativeQuery = true)
    List<Object[]> findGroupChatInfoByUserId(@Param("userId") String userId);

}
