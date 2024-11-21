//package com.example.social_network.test;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/api/images")
//public class CloudinaryController {
//
//    @Autowired
//    private CloudinaryService cloudinaryService;
//
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
//        try {
//            String imageUrl = cloudinaryService.uploadImage(file);
//            return ResponseEntity.ok(imageUrl); // Trả về URL của ảnh
//        } catch (IOException e) {
//            return ResponseEntity.status(500).body("Error uploading image: " + e.getMessage());
//        }
//    }
//    @DeleteMapping("/delete")
//    public ResponseEntity<String> deleteImage(@RequestParam("publicId") String publicId) {
//        String result = cloudinaryService.deleteImage(publicId);
//        return ResponseEntity.ok(result);  // Trả về kết quả xóa
//    }
//}
