package com.example.social_network.services;

import com.example.social_network.dtos.Request.PostRequest;
import com.example.social_network.dtos.Response.PostResponse;
import com.example.social_network.entities.Post;
import com.example.social_network.entities.PostImage;
import com.example.social_network.entities.User;
import com.example.social_network.repositories.PostImageRepository;
import com.example.social_network.repositories.PostRepository;
import com.example.social_network.repositories.UserAccountRepository;
import com.example.social_network.services.Iservice.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService implements IPostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private PostImageRepository postImageRepository;
    @Autowired
    private PostImageService postImageService;


    @Override
    @Transactional
    public PostResponse createPost(PostRequest postRequest) {
        User user = userAccountRepository.findById(postRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        Post post = Post.builder()
                .userAccount(user)
                .content(postRequest.getContent())
                .privacy(postRequest.getPrivacy() != null ? postRequest.getPrivacy() : "public")
                .build();
        Post savedPost = postRepository.save(post);
        List<PostImage> postImages = new ArrayList<>();
        if (postRequest.getImages() != null && !postRequest.getImages().isEmpty()) {
            postImages = postImageService.savePostImages(savedPost, postRequest.getImages());
        }
        List<String> imageUrls = postImages.stream()
                .map(PostImage::getImageUrl)
                .toList();
        return PostResponse.builder()
                .postId(savedPost.getPostId())
                .userId(savedPost.getUserAccount().getUserId())
                .content(savedPost.getContent())
                .privacy(savedPost.getPrivacy())
                .createAt(savedPost.getCreatedAt())
                .images(imageUrls)
                .build();
    }


    @Override
    public Post updatePost(Long postId, PostRequest postUpdateRequest) {
        return null;
    }

    @Override
    public void deletePost(Long postId) {

    }

    @Override
    public PostResponse getPostById(Long postId) {
        return null;
    }

    @Override
    public List<PostResponse> getPostsByUserId(Long userId) {
        return List.of();
    }

    @Override
    public List<PostResponse> getAllPosts(int page, int size) {
        return List.of();
    }
}
