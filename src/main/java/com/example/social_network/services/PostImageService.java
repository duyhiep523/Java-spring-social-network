package com.example.social_network.services;

import com.example.social_network.dtos.Response.PostImageResponse;
import com.example.social_network.entities.Post;
import com.example.social_network.entities.PostImage;
import com.example.social_network.entities.User;
import com.example.social_network.repositories.PostImageRepository;
import com.example.social_network.services.Iservice.IPostImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostImageService implements IPostImageService {
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


    @Override
    public List<PostImageResponse> getAllImagesByUserId(String userId) {
        List<PostImage> postImages = postImageRepository.findAllImagesByUserId(userId);
        List<PostImageResponse> postImageResponse = new ArrayList<>();

        for (PostImage postImage : postImages) {
            Post post = postImage.getPost();
            User userAccount = post.getUserAccount();
            PostImageResponse postImageResponseDTO = PostImageResponse.builder()
                    .imageId(postImage.getImageId())
                    .imageUrl(postImage.getImageUrl())
                    .postId(post.getPostId())
                    .postContent(post.getContent())
                    .userId(userAccount.getUserId())
                    .userFullName(userAccount.getFullName())
                    .userAvatarUrl(userAccount.getProfilePictureUrl())
                    .build();
            postImageResponse.add(postImageResponseDTO);
        }

        return postImageResponse;
    }
}
