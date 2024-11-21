package com.example.social_network.repositories;

import com.example.social_network.entities.GroupMessageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMessageAttachmentRepository extends JpaRepository<GroupMessageAttachment,String> {
}
