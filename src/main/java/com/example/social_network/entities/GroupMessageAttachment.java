package com.example.social_network.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "group_message_attachment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupMessageAttachment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "attachment_id",length = 50, nullable = false)
    private String attachmentId;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private GroupMessage groupMessage;

    @Column(name = "attachment_url", nullable = false)
    private String attachmentUrl;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
