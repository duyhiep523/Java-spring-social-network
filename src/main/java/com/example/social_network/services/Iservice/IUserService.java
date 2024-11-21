package com.example.social_network.services.Iservice;

public interface IUserService {

    boolean existsByUserId(String userId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
