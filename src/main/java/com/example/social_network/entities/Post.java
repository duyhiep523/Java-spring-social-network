package com.example.social_network.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "post")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "post_id", length = 50, nullable = false)
    private String postId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User userAccount;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "privacy", columnDefinition = "ENUM('public', 'private', 'friends') DEFAULT 'public'")
    private String privacy = "public";  // Default value is 'public'

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

}
