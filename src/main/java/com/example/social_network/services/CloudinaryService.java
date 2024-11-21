package com.example.social_network.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    // Phương thức mã hóa tên file và tải ảnh lên Cloudinary
    public String uploadImage(MultipartFile file) {
        try {
            // Mã hóa tên file bằng UUID và thêm phần mở rộng của file
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String encodedFileName = generateUniqueFileName(fileExtension);

            // Tải ảnh lên Cloudinary với tên file đã mã hóa
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("public_id", encodedFileName, "resource_type", "auto"));

            // Trả về URL của ảnh đã tải lên
            return (String) uploadResult.get("secure_url");
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error uploading image to Cloudinary", e);
        }
    }

    // Phương thức lấy phần mở rộng của file
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    // Phương thức tạo tên file duy nhất bằng cách mã hóa với UUID
    private String generateUniqueFileName(String fileExtension) throws NoSuchAlgorithmException {
        // Sử dụng UUID và mã hóa thành MD5 để tạo tên file duy nhất
        String uniqueName = UUID.randomUUID().toString() + "_" + System.currentTimeMillis();
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] encodedHash = digest.digest(uniqueName.getBytes());

        // Chuyển đổi hash thành chuỗi hex
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            hexString.append(String.format("%02x", b));
        }

        // Kết hợp tên file đã mã hóa với phần mở rộng
        return hexString.toString() + "." + fileExtension;
    }
}
