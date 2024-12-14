package com.example.social_network.services.Iservice;

import com.example.social_network.dtos.Response.PostImageResponse;
import com.example.social_network.entities.Post;
import com.example.social_network.entities.PostImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPostImageService {
    public List<PostImage> savePostImages(Post post, List<MultipartFile> images);

    List<PostImageResponse> getAllImagesByUserId(String userId);
}
