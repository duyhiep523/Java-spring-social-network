package com.example.social_network.services;

import com.example.social_network.dtos.Response.HighlightStoryImageResponse;
import com.example.social_network.entities.HighlightStory;
import com.example.social_network.entities.HighlightStoryImage;
import com.example.social_network.entities.Post;
import com.example.social_network.entities.PostImage;
import com.example.social_network.repositories.HighlightStoryImageRepository;
import com.example.social_network.repositories.HighlightStoryRepository;
import com.example.social_network.services.Iservice.IHighlightStoryImageService;
import com.example.social_network.services.Iservice.IHighlightStoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class HighlightStoryImageService implements IHighlightStoryImageService {
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private HighlightStoryImageRepository highlightStoryImageRepository;
    @Autowired
    private HighlightStoryRepository highlightStoryRepository;

    @Override
    public List<HighlightStoryImage> savePostImages(HighlightStory highlightStory, List<MultipartFile> images) {
        List<HighlightStoryImage> highlightStoryImages = new ArrayList<>();
        if (images == null || images.isEmpty()) {
            return new ArrayList<>();
        }
        for (MultipartFile image : images) {
            String imageUrl = cloudinaryService.uploadImage(image);
            HighlightStoryImage highlightStoryImage = new HighlightStoryImage();
            highlightStoryImage.setHighlightStory(highlightStory);
            highlightStoryImage.setImageUrl(imageUrl);
            highlightStoryImage.setIsDeleted(false);
            highlightStoryImageRepository.save(highlightStoryImage);
            highlightStoryImages.add(highlightStoryImage);
        }
        return highlightStoryImages;
    }

    @Override
    public HighlightStoryImageResponse addHighlightStoryImage(String highlightStoryId, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new RuntimeException("Không có ảnh để thêm.");
        }

        HighlightStory highlightStory = highlightStoryRepository.findById(highlightStoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy HighlightStory với ID: " + highlightStoryId));

        // Upload ảnh lên Cloudinary
        String imageUrl = cloudinaryService.uploadImage(image);


        HighlightStoryImage highlightStoryImage = HighlightStoryImage.builder()
                .highlightStory(highlightStory)
                .imageUrl(imageUrl)
                .isDeleted(false)
                .build();
        highlightStoryImageRepository.save(highlightStoryImage);
        // Trả về response
        return HighlightStoryImageResponse.builder()
                .storyId(highlightStoryImage.getHighlightStory().getStoryId())
                .imageId(highlightStoryImage.getImageId())
                .imageUrl(highlightStoryImage.getImageUrl())
                .isDeleted(highlightStoryImage.getIsDeleted())
                .build();
    }


    @Override
    public void softDeleteHighlightStoryImage(String imageId) {
        HighlightStoryImage highlightStoryImage = highlightStoryImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Highlight Story Image không tồn tại"));
        highlightStoryImage.setIsDeleted(true);
        highlightStoryImageRepository.save(highlightStoryImage);
    }


    @Override
    public List<HighlightStoryImageResponse> getHighlightStoryImagesByStoryId(String highlightStoryId) {
        HighlightStory highlightStory = highlightStoryRepository.findById(highlightStoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy HighlightStory với ID: " + highlightStoryId));
        List<HighlightStoryImage> highlightStoryImages = highlightStoryImageRepository.findByHighlightStory(highlightStory);

        List<HighlightStoryImageResponse> responseList = new ArrayList<>();
        for (HighlightStoryImage highlightStoryImage : highlightStoryImages) {
            HighlightStoryImageResponse response = HighlightStoryImageResponse.builder()
                    .storyId(highlightStoryId)
                    .imageId(highlightStoryImage.getImageId())
                    .imageUrl(highlightStoryImage.getImageUrl())
                    .isDeleted(highlightStoryImage.getIsDeleted())
                    .build();
            responseList.add(response);
        }

        return responseList;
    }


}
