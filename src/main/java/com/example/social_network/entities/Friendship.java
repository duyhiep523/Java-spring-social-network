package com.example.social_network.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "friendship")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Friendship extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "friendship_id",length = 50, nullable = false)
    private String friendshipId;

    @ManyToOne
    @JoinColumn(name = "user_id_1", referencedColumnName = "user_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user_id_2", referencedColumnName = "user_id")
    private User user2;

    @Column(name = "status", length = 20, nullable = false)
    private String status = "PENDING";  // Default value is 'PENDING'
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
