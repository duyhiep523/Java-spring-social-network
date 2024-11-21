package com.example.social_network.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "message_id",length = 50, nullable = false)
    private String messageId;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private GroupChat groupChat;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id")
    private User sender;

    @Column(name = "message_content", columnDefinition = "LONGTEXT")
    private String messageContent;

    @Column(name = "message_type", length = 20, nullable = false)
    private String messageType = "TEXT";  // Default value is 'TEXT'
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
