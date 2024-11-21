package com.example.social_network.services;

import com.example.social_network.entities.Post;
import com.example.social_network.entities.PostImage;
import com.example.social_network.repositories.PostImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostImageService {
    private final CloudinaryService cloudinaryService;
    private final PostImageRepository postImageRepository;

    public List<PostImage> savePostImages(Post post, List<MultipartFile> images) {
        List<PostImage> postImages = new ArrayList<>();
        if (images == null || images.isEmpty()) {
            return new ArrayList<>();
        }
        for (MultipartFile image : images) {

            String imageUrl = cloudinaryService.uploadImage(image);
            PostImage postImage = new PostImage();
            postImage.setPost(post);
            postImage.setImageUrl(imageUrl);
            postImageRepository.save(postImage);
            postImages.add(postImage);
        }
        return postImages;
    }
}
