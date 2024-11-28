package com.example.social_network.entities;

import com.example.social_network.comon.enums.PostReactionEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_reaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostReaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "reaction_id", length = 50, nullable = false)
    private String reactionId;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User userAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type", nullable = false, columnDefinition = "ENUM('LIKE', 'LOVE', 'HAHA', 'WOW', 'SAD', 'ANGRY') NOT NULL")
    private PostReactionEnum reactionType;


    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
