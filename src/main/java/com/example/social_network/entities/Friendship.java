package com.example.social_network.entities;

import com.example.social_network.comon.enums.FriendshipStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "friendship")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Friendship extends BaseEntity {

    @Id
    @Column(name = "friendship_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String friendshipId;

    @ManyToOne
    @JoinColumn(name = "user_id_1", referencedColumnName = "user_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user_id_2", referencedColumnName = "user_id")
    private User user2;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;


    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}
