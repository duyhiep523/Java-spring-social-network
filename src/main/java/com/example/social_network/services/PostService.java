package com.example.social_network.services;

import com.example.social_network.dtos.Request.PostRequest;
import com.example.social_network.dtos.Request.PostUpdateRequest;
import com.example.social_network.dtos.Response.NewsFeed;
import com.example.social_network.dtos.Response.PostResponse;
import com.example.social_network.dtos.Response.PostResponseDetail;
import com.example.social_network.entities.Post;
import com.example.social_network.entities.PostImage;
import com.example.social_network.entities.User;
import com.example.social_network.exceptions.ResourceNotFoundException;
import com.example.social_network.repositories.*;
import com.example.social_network.services.Iservice.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private PostCommentRepository postCommentRepository;
    @Autowired
    private PostReactionRepository postReactionRepository;

    @Override
    @Transactional
    public PostResponse createPost(PostRequest postRequest) {
        User user = userAccountRepository.findById(postRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        Post post = Post.builder()
                .userAccount(user)
                .content(postRequest.getContent())
                .privacy(postRequest.getPrivacy() != null ? postRequest.getPrivacy() : "public")
                .theme(postRequest.getTheme())
                .share(postRequest.getShare())
                .isDeleted(false)
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
                .theme(savedPost.getTheme())
                .share(savedPost.getShare())
                .numOfShare(savedPost.getNumOfShare())
                .isDeleted(false)
                .build();
    }


    @Override
    public PostResponseDetail getPostById(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Bài viết không tồn tại"));

        long commentCount = postCommentRepository.countByPost_PostId(postId);
        long reactionCount = postReactionRepository.countByPost_PostId(postId);


        List<String> imageUrls = postImageRepository.findByPost_PostId(postId).stream()
                .map(PostImage::getImageUrl)
                .toList();


        PostResponse postResponse = PostResponse
                .builder()
                .postId(post.getPostId())
                .userId(post.getUserAccount().getUserId())
                .content(post.getContent())
                .privacy(post.getPrivacy())
                .createAt(post.getCreatedAt())
                .images(imageUrls)
                .isDeleted(post.getIsDeleted())
                .theme(post.getTheme())
                .share(post.getShare())
                .numOfShare(post.getNumOfShare())//
                .build();
        return PostResponseDetail.builder()
                .postResponse(postResponse)
                .commentCount(commentCount)
                .reactionCount(reactionCount)
                .build();
    }

    @Override
    @Transactional
    public void deletePost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Bài viết không tồn tại"));
        post.setIsDeleted(true);
        postRepository.save(post);
    }


    @Override
    @Transactional
    public PostResponse updatePost(String postId, PostUpdateRequest postUpdateRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Bài viết không tồn tại"));


        post.setContent(postUpdateRequest.getContent());
        post.setPrivacy(postUpdateRequest.getPrivacy() != null ? postUpdateRequest.getPrivacy() : post.getPrivacy());
        post.setTheme(postUpdateRequest.getTheme());

        //  Cập nhật ảnh nếu có
        if (postUpdateRequest.getImages() != null && !postUpdateRequest.getImages().isEmpty()) {
            // Xoá ảnh cũ
            postImageRepository.deleteByPost_PostId(postId);
            List<PostImage> newImages = postImageService.savePostImages(post, postUpdateRequest.getImages());

        }

        // Lưu bài viết cập nhật
        Post updatedPost = postRepository.save(post);

        List<String> imageUrls = postImageRepository.findByPost_PostId(postId).stream()
                .map(PostImage::getImageUrl)
                .toList();


        return PostResponse.builder()
                .postId(updatedPost.getPostId())
                .userId(updatedPost.getUserAccount().getUserId())
                .content(updatedPost.getContent())
                .privacy(updatedPost.getPrivacy())
                .createAt(updatedPost.getCreatedAt())
                .images(imageUrls)
                .isDeleted(updatedPost.getIsDeleted())
                .theme(updatedPost.getTheme())
                .share(updatedPost.getShare())
                .numOfShare(post.getNumOfShare())//
                .build();
    }

    @Override
    public NewsFeed getAllPostsByUserIdFromFriend(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("create_at")));
        Page<Post> posts = postRepository.findAllPostsByFriendship(userId, pageable);
        List<PostResponse> postResponses = posts.getContent().stream()
                .map(this::convertToPostResponse)
                .collect(Collectors.toList());
        return new NewsFeed(postResponses, posts.getTotalElements(), posts.getTotalPages(), posts.getNumber(), posts.getSize());
    }

    private PostResponse convertToPostResponse(Post post) {
        List<String> imageUrls = postImageRepository.findByPost_PostId(post.getPostId()).stream()
                .map(PostImage::getImageUrl)
                .toList();

        return PostResponse.builder()
                .postId(post.getPostId())
                .content(post.getContent())
                .privacy(post.getPrivacy())
                .theme(post.getTheme())
                .share(post.getShare())
                .userId(post.getUserAccount().getUserId())
                .createAt(post.getCreatedAt())
                .images(imageUrls)
                .isDeleted(post.getIsDeleted())
                .numOfShare(post.getNumOfShare())//
                .build();
    }


    @Override
    public NewsFeed getAllPostsByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));

        Page<Post> posts = postRepository.findPostsByUserId(userId, pageable);

        List<PostResponse> postResponses = posts.getContent().stream()
                .map(this::convertToPostResponse)
                .collect(Collectors.toList());
        return new NewsFeed(
                postResponses,
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.getNumber(),
                posts.getSize()
        );
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

    @Override
    @Transactional
    public int incrementNumOfShare(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Bài viết không tồn tại"));

        // Xử lý trường hợp numOfShare null
        if (post.getNumOfShare() == null) {
            post.setNumOfShare(0);
        }

        post.setNumOfShare(post.getNumOfShare() + 1);
        Post updatedPost = postRepository.save(post);
        return updatedPost.getNumOfShare();
    }

    @Override
    public int getShareCount(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Bài viết không tồn tại"));
        return post.getNumOfShare();
    }

}
