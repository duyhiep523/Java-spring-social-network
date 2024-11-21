package com.example.social_network.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "highlight_story_image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HighlightStoryImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "image_id",length = 50, nullable = false)
    private String imageId;

    @ManyToOne
    @JoinColumn(name = "story_id", referencedColumnName = "story_id")
    private HighlightStory highlightStory;

    @Column(name = "image_url", length = 255, nullable = false)
    private String imageUrl;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
