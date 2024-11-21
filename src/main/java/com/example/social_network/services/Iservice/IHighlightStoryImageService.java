package com.example.social_network.services.Iservice;

import com.example.social_network.dtos.Response.HighlightStoryImageResponse;
import com.example.social_network.entities.HighlightStory;
import com.example.social_network.entities.HighlightStoryImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IHighlightStoryImageService {

    List<HighlightStoryImage> savePostImages(HighlightStory highlightStory, List<MultipartFile> images);
    void softDeleteHighlightStoryImage(String imageId);
    HighlightStoryImageResponse addHighlightStoryImage(String highlightStoryId, MultipartFile image);
    List<HighlightStoryImageResponse> getHighlightStoryImagesByStoryId(String highlightStoryId);
}
