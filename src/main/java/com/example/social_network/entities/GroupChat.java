package com.example.social_network.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_chat")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupChat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "group_id",length = 50, nullable = false)
    private String groupId;

    @Column(name = "group_name", length = 100, nullable = false)
    private String groupName;

    @Column(name = "group_image_url", length = 255)
    private String groupImageUrl;
    @Column(name = "theme", length = 50)
    private String theme;
    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "user_id")
    private User admin;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
