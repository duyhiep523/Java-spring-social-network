package com.example.social_network.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "image_id",length = 50, nullable = false)
    private String imageId;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private Post post;

    @Column(name = "image_url", length = 255, nullable = false)
    private String imageUrl;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
