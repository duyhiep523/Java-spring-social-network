package com.example.social_network.repositories;

import com.example.social_network.entities.SecurityToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityTokenRepository extends JpaRepository<SecurityToken,String> {
}