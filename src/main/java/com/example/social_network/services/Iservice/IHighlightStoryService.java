package com.example.social_network.services.Iservice;


import com.example.social_network.dtos.Request.HighlightStoryRequest;
import com.example.social_network.dtos.Response.HighlightStoryDetailResponse;
import com.example.social_network.dtos.Response.HighlightStoryResponse;
import com.example.social_network.entities.HighlightStory;

import java.util.List;

public interface IHighlightStoryService {
    HighlightStoryResponse createHighlightStory(HighlightStoryRequest highlightStoryRequest);
    void deleteHighlightStory(String postId);
    HighlightStory getHighlightStoryById(String highlightStoryId);
    void updateHighlightStory(String highlightStoryId, String highlightStoryName);
    HighlightStory findHighlightStoryById(String highlightStoryId);
    HighlightStoryDetailResponse getHighlightStoryDetail(String highlightStoryId);
    List<HighlightStoryDetailResponse> getAllHighlightStoryDetails();
}
