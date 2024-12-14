package com.example.social_network.dtos.Request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateRequest {

    private String content;

    private String privacy = null;

    private String theme=null;

    private List<MultipartFile> images;
}
