package com.example.social_network.repositories;

import com.example.social_network.entities.HighlightStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HighlightStoryRepository extends JpaRepository<HighlightStory,String> {
    List<HighlightStory> findByUserAccount_UserId(String userId);

}
