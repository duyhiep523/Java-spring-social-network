package com.example.social_network.repositories;

import com.example.social_network.entities.HighlightStory;
import com.example.social_network.entities.HighlightStoryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HighlightStoryImageRepository extends JpaRepository<HighlightStoryImage, String> {
    List<HighlightStoryImage> findByHighlightStory(HighlightStory highlightStory);

}
