package com.example.social_network.repositories;

import com.example.social_network.entities.PrivateMessageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivateMessageAttachmentRepository extends JpaRepository<PrivateMessageAttachment,String> {
}
