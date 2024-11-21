package com.example.social_network.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "highlight_story")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HighlightStory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "story_id",length = 50, nullable = false)
    private String storyId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User userAccount;

    @Column(name = "story_name", length = 100, nullable = false)
    private String storyName;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
