//package com.example.social_network.test;
//
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.UUID;
//
////@Service
//public class CloudinaryService {
//
//    @Autowired
//    private Cloudinary cloudinary;
//
//    // Phương thức tạo tên ngẫu nhiên cho ảnh
//    private String generateUniquePublicId() {
//        // Sử dụng UUID để tạo tên duy nhất cho ảnh
//        return "image_" + UUID.randomUUID().toString();
//    }
//
//    // Phương thức tải ảnh lên Cloudinary với tên ngẫu nhiên
//    public String uploadImage(MultipartFile file) throws IOException {
//        String uniquePublicId = generateUniquePublicId();  // Tạo tên ngẫu nhiên cho ảnh
//        try {
//            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(),
//                    ObjectUtils.asMap("public_id", uniquePublicId));
//            return (String) uploadResult.get("url");  // Trả về URL của ảnh đã tải lên
//        } catch (IOException e) {
//            throw new IOException("Error uploading image: " + e.getMessage());
//        }
//    }
//
//    // Phương thức xóa ảnh khỏi Cloudinary theo public_id
//    public String deleteImage(String publicId) {
//        try {
//            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
//
//            // Kiểm tra kết quả trả về từ Cloudinary
//            if (result.containsKey("result") && "ok".equals(result.get("result"))) {
//                return "Image deleted successfully.";
//            } else {
//                // Trả về thông báo lỗi nếu kết quả không phải "ok"
//                return "Failed to delete image. Result: " + result.get("result");
//            }
//        } catch (IOException e) {
//            // Bắt lỗi IOException và trả về thông báo lỗi chi tiết
//            return "Error deleting image: " + e.getMessage();
//        }
//    }
//
//}
