package com.example.social_network.services;

import com.example.social_network.comon.enums.PostReactionEnum;
import com.example.social_network.dtos.Response.PostReactionResponse;
import com.example.social_network.dtos.Response.UserReactionResponse;
import com.example.social_network.entities.Post;
import com.example.social_network.entities.PostReaction;
import com.example.social_network.entities.User;
import com.example.social_network.exceptions.ResourceNotFoundException;
import com.example.social_network.repositories.PostReactionRepository;
import com.example.social_network.repositories.PostRepository;
import com.example.social_network.repositories.UserAccountRepository;
import com.example.social_network.services.Iservice.IPostReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostReactionService implements IPostReactionService {
    @Autowired
    private PostReactionRepository postReactionRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public PostReactionResponse addReaction(String postId, String userId, PostReactionEnum reactionType) {


        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new ResourceNotFoundException("Bài viết không tồn tại.");
        }

        // Kiểm tra người dùng có tồn tại không
        Optional<User> userOptional = userAccountRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("Người dùng không tồn tại.");
        }
        Optional<PostReaction> existingReaction = postReactionRepository.findByPost_PostIdAndUserAccount_UserId(postId, userId);

        if (existingReaction.isPresent()) {

            PostReaction reaction = existingReaction.get();
            if (reaction.getIsDeleted()) {
                reaction.setIsDeleted(false);
            }
            reaction.setReactionType(reactionType);
            return toResponse(postReactionRepository.save(reaction));
        }


        PostReaction newReaction = PostReaction.builder()
                .post(Post.builder().postId(postId).build())
                .userAccount(User.builder().userId(userId).build())
                .reactionType(reactionType)
                .isDeleted(false)
                .build();

        return toResponse(postReactionRepository.save(newReaction));
    }

    @Override
    public void deleteReaction(String postId, String userId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new ResourceNotFoundException("Bài viết không tồn tại.");
        }


        Optional<User> userOptional = userAccountRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("Người dùng không tồn tại.");
        }
        PostReaction reaction = postReactionRepository
                .findByPost_PostIdAndUserAccount_UserId(postId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phản ứng cho bài viết này của người dùng"));

        postReactionRepository.delete(reaction);
    }


    @Override
    public long countReactionsForPost(String postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new ResourceNotFoundException("Bài viết không tồn tại.");
        }
        return postReactionRepository.countByPost_PostId(postId);
    }

    @Override
    public List<UserReactionResponse> getReactionsForPost(String postId) {

        List<PostReaction> reactions = postReactionRepository.findByPost_PostId(postId);


        return reactions.stream()
                .map(reaction -> {
                    User user = reaction.getUserAccount();
                    return UserReactionResponse.builder()
                            .userId(user.getUserId())
                            .fullName(user.getFullName())
                            .avatarUrl(user.getProfilePictureUrl())  // Giả sử User có trường avatarUrl
                            .reactionType(reaction.getReactionType())
                            .build();
                })
                .collect(Collectors.toList());
    }


    private PostReactionResponse toResponse(PostReaction postReaction) {
        return PostReactionResponse.builder()
                .reactionId(postReaction.getReactionId())
                .postId(postReaction.getPost().getPostId())
                .userId(postReaction.getUserAccount().getUserId())
                .reactionType(postReaction.getReactionType())
                .isDeleted(postReaction.getIsDeleted())
                .build();
    }


}
