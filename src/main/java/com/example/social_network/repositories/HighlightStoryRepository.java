package com.example.social_network.repositories;

import com.example.social_network.entities.HighlightStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HighlightStoryRepository extends JpaRepository<HighlightStory,String> {
}
