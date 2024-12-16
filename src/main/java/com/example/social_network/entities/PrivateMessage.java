package com.example.social_network.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "private_message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrivateMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "message_id",length = 50, nullable = false)
    private String messageId;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
    private User receiver;

    @Column(name = "message_content", columnDefinition = "TEXT")
    private String messageContent;
    @Column(name = "attachment_url", nullable = false)
    private String attachmentUrl;
    @Column(name = "message_type", length = 20, nullable = false)
    private String messageType = "TEXT";
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
