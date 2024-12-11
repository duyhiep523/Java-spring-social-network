package com.example.social_network.services;

import com.example.social_network.dtos.Request.HighlightStoryRequest;
import com.example.social_network.dtos.Response.HighlightStoryDetailResponse;
import com.example.social_network.dtos.Response.HighlightStoryImageResponse;
import com.example.social_network.dtos.Response.HighlightStoryResponse;
import com.example.social_network.entities.*;
import com.example.social_network.exceptions.ResourceNotFoundException;
import com.example.social_network.repositories.HighlightStoryImageRepository;
import com.example.social_network.repositories.HighlightStoryRepository;
import com.example.social_network.repositories.UserAccountRepository;
import com.example.social_network.services.Iservice.IHighlightStoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class HighlightStoryService implements IHighlightStoryService {
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private HighlightStoryRepository highlightStoryRepository;
    @Autowired
    private HighlightStoryImageService highlightStoryImageService;
    @Autowired
    private HighlightStoryImageRepository highlightStoryImageRepository;

    @Override
    @Transactional
    public HighlightStoryResponse createHighlightStory(HighlightStoryRequest highlightStoryRequest) {
        User user = userAccountRepository.findById(highlightStoryRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        HighlightStory newHighlightStory = HighlightStory.builder()
                .userAccount(user)
                .storyName(highlightStoryRequest.getStoryName())
                .isDeleted(false)
                .build();
        HighlightStory savedHighlightStory = highlightStoryRepository.save(newHighlightStory);
        List<HighlightStoryImage> highlightStoryImages = new ArrayList<>();
        if (highlightStoryRequest.getImages() != null && !highlightStoryRequest.getImages().isEmpty()) {
            highlightStoryImages = highlightStoryImageService.savePostImages(savedHighlightStory, highlightStoryRequest.getImages());
        }
        List<String> imageUrls = highlightStoryImages.stream()
                .map(HighlightStoryImage::getImageUrl)
                .toList();
        return HighlightStoryResponse.builder()

                .userId(savedHighlightStory.getUserAccount().getUserId())
                .storyName(savedHighlightStory.getStoryName())
                .createAt(savedHighlightStory.getCreatedAt())
                .images(imageUrls)
                .build();
    }


    @Override
    public void updateHighlightStory(String highlightStoryId, String highlightStoryName) {
        HighlightStory highlightStory = getHighlightStoryById(highlightStoryId);
        if (highlightStoryName != null && !highlightStoryName.isEmpty()) {
            highlightStory.setStoryName(highlightStoryName);
        }
        HighlightStory updatedHighlightStory = highlightStoryRepository.save(highlightStory);
        HighlightStoryResponse.builder()
                .storyName(updatedHighlightStory.getStoryName())
                .build();
    }

    @Override
    public void deleteHighlightStory(String highlightStoryId) {
        HighlightStory highlightStory = findHighlightStoryById(highlightStoryId);
        if (highlightStory.getIsDeleted()) {
            throw new RuntimeException("HighlightStory đã bị xóa");
        }
        highlightStory.setIsDeleted(true);
        highlightStoryRepository.save(highlightStory);
    }

    @Override
    public HighlightStory findHighlightStoryById(String highlightStoryId) {
        return highlightStoryRepository.findById(highlightStoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy HighlightStory"));
    }


    @Override
    public HighlightStory getHighlightStoryById(String highlightStoryId) {
        return highlightStoryRepository.findById(highlightStoryId)
                .orElseThrow(() -> new RuntimeException("HighlightStory không tồn tại với ID: " + highlightStoryId));
    }

    @Override
    public HighlightStoryDetailResponse getHighlightStoryDetail(String highlightStoryId) {

        HighlightStory highlightStory = highlightStoryRepository.findById(highlightStoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy HighlightStory với ID: " + highlightStoryId));

        List<HighlightStoryImageResponse> highlightStoryImages = highlightStoryImageService.getHighlightStoryImagesByStoryId(highlightStoryId);
        return HighlightStoryDetailResponse.builder()

                .storyId(highlightStoryId)
                .userId(highlightStory.getUserAccount().getUserId())
                .storyName(highlightStory.getStoryName())
                .createAt(highlightStory.getCreatedAt())
                .highlightStoryImageResponse(highlightStoryImages)
                .isDeleted(highlightStory.getIsDeleted())
                .build();
    }

    @Override
    public List<HighlightStoryDetailResponse> getAllHighlightStoryDetails(String userId) {
        userAccountRepository.findById(userId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Người dùng không tồn tại"));


        List<HighlightStory> highlightStories = highlightStoryRepository.findByUserAccount_UserId(userId);
        return highlightStories.stream().map(highlightStory -> {
            List<HighlightStoryImage> images = highlightStoryImageRepository.findByHighlightStory(highlightStory);
            List<HighlightStoryImageResponse> imageResponses = images.stream().map(image ->
                    HighlightStoryImageResponse.builder()
                            .storyId(image.getHighlightStory().getStoryId())
                            .imageId(image.getImageId())
                            .imageUrl(image.getImageUrl())
                            .isDeleted(image.getIsDeleted())
                            .build()
            ).toList();

            return HighlightStoryDetailResponse.builder()
                    .storyId(highlightStory.getStoryId())
                    .userId(highlightStory.getUserAccount().getUserId())
                    .storyName(highlightStory.getStoryName())
                    .createAt(highlightStory.getCreatedAt())
                    .isDeleted(highlightStory.getIsDeleted())
                    .highlightStoryImageResponse(imageResponses)
                    .build();
        }).toList();
    }

}
