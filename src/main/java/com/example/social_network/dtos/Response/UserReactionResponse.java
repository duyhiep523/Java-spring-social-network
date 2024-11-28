package com.example.social_network.dtos.Response;

import com.example.social_network.comon.enums.PostReactionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReactionResponse {

    private String userId;
    private String fullName;  // Tên người dùng
    private String avatarUrl; // Đường dẫn ảnh đại diện người dùng
    private PostReactionEnum reactionType; // Loại cảm xúc mà người dùng đã thả
}
