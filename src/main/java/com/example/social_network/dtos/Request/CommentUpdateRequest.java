package com.example.social_network.dtos.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateRequest {
    @NotBlank(message = "ID bình luận không được để trống")
    private String commentId;

    @NotBlank(message = "ID người dùng không được để trống")
    private String userId;

    @NotBlank(message = "Nội dung bình luận không được trống")
    private String newContent;
}
