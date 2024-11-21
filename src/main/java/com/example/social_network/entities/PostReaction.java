package com.example.social_network.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_reaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostReaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "reaction_id",length = 50, nullable = false)
    private String reactionId;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User userAccount;

    @Column(name = "reaction_type", length = 20, nullable = false)
    private String reactionType; // LIKE, LOVE, HAHA, WOW, SAD, ANGRY
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
