package com.example.social_network.entities;

import com.example.social_network.comon.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id",length = 50, nullable = false, unique = true)
    private String userId;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(name = "profile_picture_url", length = 255)
    private String profilePictureUrl;

    @Column(name = "cover_picture_url", length = 255)
    private String coverPictureUrl;


    @Column(name = "bio",length = 255)
    private String bio;

    @Column(name = "hometown", length = 255)
    private String hometown;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, columnDefinition = "ENUM('MALE', 'FEMALE', 'OTHER') DEFAULT 'OTHER'")
    private Gender gender;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
