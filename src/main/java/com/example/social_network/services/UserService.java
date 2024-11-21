package com.example.social_network.services;

import com.example.social_network.entities.User;
import com.example.social_network.repositories.UserAccountRepository;
import com.example.social_network.services.Iservice.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserAccountRepository userRepository;
    @Autowired
    private com.example.social_network.services.CloudinaryService cloudinaryService;

    @Override
    public boolean existsByUserId(String userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public boolean existsByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

}
