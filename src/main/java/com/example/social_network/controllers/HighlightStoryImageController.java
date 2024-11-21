package com.example.social_network.controllers;

import com.example.social_network.dtos.Response.HighlightStoryImageResponse;
import com.example.social_network.response.Error;
import com.example.social_network.response.Response;
import com.example.social_network.services.Iservice.IHighlightStoryImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/highlightStoryImage")
@RequiredArgsConstructor
public class HighlightStoryImageController {

    private final IHighlightStoryImageService highlightStoryImageService;

    @PostMapping("/add/{highlightStoryId}")
    public ResponseEntity<?> addHighlightStoryImages(
            @PathVariable String highlightStoryId,
            @RequestParam("image") MultipartFile image) {
        HighlightStoryImageResponse highlightStoryImageResponses = highlightStoryImageService.addHighlightStoryImage(highlightStoryId, image);
        Response<Object> response = Response.builder()
                .message("Thêm ảnh thành công")
                .data(highlightStoryImageResponses)
                .success(true)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Xóa ảnh (soft delete)
    @DeleteMapping("/softDelete/{imageId}")
    public ResponseEntity<?> softDeleteHighlightStoryImage(@PathVariable String imageId) {
        highlightStoryImageService.softDeleteHighlightStoryImage(imageId);
        Response<Void> response = new Response<>();
        response.setSuccess(true);
        response.setMessage("Xóa mềm ảnh thành công");
        return ResponseEntity.ok(response);
    }

}
