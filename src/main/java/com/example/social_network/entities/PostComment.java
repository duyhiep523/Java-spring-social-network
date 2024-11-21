package com.example.social_network.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_comment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "comment_id",length = 50, nullable = false)
    private String commentId;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User userAccount;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id", referencedColumnName = "comment_id")
    private PostComment parentComment;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
