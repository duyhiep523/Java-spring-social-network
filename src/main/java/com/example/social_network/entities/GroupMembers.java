package com.example.social_network.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "group_members")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupMembers extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "group_member_id",length = 50, nullable = false)
    private String groupMemberId;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private GroupChat groupChat;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userAccount;

    @Column(name = "join_date")
    private LocalDateTime joinDate;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
