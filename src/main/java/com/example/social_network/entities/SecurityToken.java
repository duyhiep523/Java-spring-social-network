package com.example.social_network.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "security_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SecurityToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "token_id",length = 50, nullable = false)
    private String tokenId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User userAccount;

    @Column(name = "access_token", length = 255, nullable = false)
    private String accessToken;

    @Column(name = "expiration")
    private LocalDateTime expiration;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
