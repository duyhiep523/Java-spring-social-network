package com.example.social_network.controllers;

import com.example.social_network.dtos.Request.HighlightStoryRequest;
import com.example.social_network.dtos.Request.PostRequest;
import com.example.social_network.dtos.Request.UpdateNameHighlightStoryRequest;
import com.example.social_network.dtos.Response.HighlightStoryDetailResponse;
import com.example.social_network.dtos.Response.HighlightStoryResponse;
import com.example.social_network.dtos.Response.PostResponse;
import com.example.social_network.response.Error;
import com.example.social_network.response.Response;
import com.example.social_network.services.Iservice.IHighlightStoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/highlightStory")
@RequiredArgsConstructor
public class HighlightStoryController {
    private final IHighlightStoryService highlightStoryService;

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@Valid @ModelAttribute HighlightStoryRequest highlightStoryRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String error = errorMessages.toString();
            Error errorResponse = new Error(error, "422");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        HighlightStoryResponse createdHighlightStoryResponse = highlightStoryService.createHighlightStory(highlightStoryRequest);
        Response<Object> response = Response.builder()
                .message("story tạo thành công")
                .data(createdHighlightStoryResponse)
                .success(true)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("updateName/{id}")
    public ResponseEntity<?> updateHighlightStory(
            @PathVariable String id,
            @Valid @RequestBody UpdateNameHighlightStoryRequest update,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String error = errorMessages.toString();
            Error errorResponse = new Error(error, "422");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        highlightStoryService.updateHighlightStory(id, update.getStoryName());
        Response<Object> response = Response.builder()
                .message("Cập nhật tên story thành công")
                .data(null)
                .success(true)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteHighlightStory(@PathVariable("id") String highlightStoryId) {
        highlightStoryService.deleteHighlightStory(highlightStoryId);
        Response<Void> response = new Response<>();
        response.setSuccess(true);
        response.setMessage("Xóa HighlightStory thành công");
        return ResponseEntity.ok(response);
    }

    @GetMapping("getDetail/{id}")
    public ResponseEntity<?> getHighlightStoryDetail(@PathVariable String id) {
        HighlightStoryDetailResponse detailResponse = highlightStoryService.getHighlightStoryDetail(id);
        Response<Object> response = Response.builder()
                .message("Lấy chi tiết HighlightStory thành công")
                .data(detailResponse)
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/allDetails/{userId}")
    public ResponseEntity<?> getAllHighlightStoryDetails(@PathVariable String userId) {
        List<HighlightStoryDetailResponse> highlightStoryDetails = highlightStoryService.getAllHighlightStoryDetails(userId);
        Response<Object> response = Response.builder()
                .message("Lấy danh sách chi tiết tất cả HighlightStory thành công")
                .data(highlightStoryDetails)
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }


}
