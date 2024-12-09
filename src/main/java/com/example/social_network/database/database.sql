DROP DATABASE IF EXISTS social_media_db;
CREATE DATABASE social_media_db;
USE social_media_db;
-- bang nguoi dung he thong 
CREATE TABLE `user_account` (
    `user_id` varchar(50) PRIMARY KEY ,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `full_name` VARCHAR(100) NOT NULL,
    `profile_picture_url` VARCHAR(255) DEFAULT NULL,
    `cover_picture_url` VARCHAR(255) DEFAULT NULL,
    `bio` varchar(255) DEFAULT NULL,
    `gender`  ENUM('MALE', 'FEMALE', 'OTHER')  DEFAULT 'OTHER',
    `hometown` VARCHAR(255) DEFAULT NULL, 
    `date_of_birth` DATE DEFAULT NULL, 
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(255),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE
);

-- refreshtoken
CREATE TABLE `security_token` (
    `token_id` varchar(50) PRIMARY KEY ,
    `user_id`  varchar(50),
    `access_token` VARCHAR(255) NOT NULL,
    `expiration` DATETIME,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user_account`(`user_id`) 
);

-- bang bai viet chung
CREATE TABLE `post` (
    `post_id` varchar(50) PRIMARY KEY ,
    `user_id`  varchar(50),
    `content` TEXT,
    `privacy` ENUM('public', 'private', 'friends')  DEFAULT 'public',  
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(255),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`user_id`) REFERENCES `user_account`(`user_id`)
);

-- lưu thông tin về file phương tiện của bài viết
CREATE TABLE `post_image` (
    `image_id` varchar(50) PRIMARY KEY ,
    `post_id`  varchar(50),
    `image_url` VARCHAR(255) NOT NULL,
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(255),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`post_id`) REFERENCES `post`(`post_id`)
);
-- thả cảm xúc 
CREATE TABLE `post_reaction` (
    `reaction_id`  varchar(50) PRIMARY KEY ,
    `post_id` varchar(50),
    `user_id` varchar(50),
    `reaction_type` ENUM('LIKE', 'LOVE', 'HAHA', 'WOW', 'SAD', 'ANGRY') NOT NULL,
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(255),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`post_id`) REFERENCES `post`(`post_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user_account`(`user_id`)
);

-- comment về bài viết
CREATE TABLE `post_comment` (
    `comment_id`  varchar(50) PRIMARY KEY ,
    `post_id` varchar(50),                  
    `user_id` varchar(50),                
    `content` TEXT NOT NULL,         
    `parent_comment_id` varchar(50) DEFAULT NULL,  
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP, 
    `create_by` varchar(255), 
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  
    `update_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE, 
    FOREIGN KEY (`post_id`) REFERENCES `post`(`post_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user_account`(`user_id`),
    FOREIGN KEY (`parent_comment_id`) REFERENCES `post_comment`(`comment_id`)
);
-- quan hệ bạn bè 
CREATE TABLE `friendship` (
    `friendship_id` varchar(50) PRIMARY KEY ,
    `user_id_1` varchar(50),
    `user_id_2` varchar(50),
    `status` ENUM('PENDING', 'ACCEPTED', 'BlOCK') DEFAULT 'PENDING',
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(255),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`user_id_1`) REFERENCES `user_account`(`user_id`),
    FOREIGN KEY (`user_id_2`) REFERENCES `user_account`(`user_id`)
);
-- nhóm chat 
CREATE TABLE `group_chat` (
    `group_id`  varchar(50) PRIMARY KEY ,
    `group_name` VARCHAR(100) NOT NULL,
    `group_image_url` VARCHAR(255) DEFAULT NULL,
    `admin_id` varchar(50) not null,
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(255),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`admin_id`) REFERENCES `user_account`(`user_id`)
);
-- thành viên trong đoạn chat
CREATE TABLE `group_members` (
    `group_member_id`  varchar(50) PRIMARY KEY ,
    `group_id` varchar(50),
    `user_id` varchar(50),
    `join_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(255),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`group_id`) REFERENCES `group_chat`(`group_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user_account`(`user_id`)
);
CREATE TABLE `group_message` (
    `message_id`  varchar(50) PRIMARY KEY ,
    `group_id` varchar(50),
    `sender_id` varchar(50),
    `message_content` TEXT,
    `message_type` ENUM('TEXT', 'IMAGE') NOT NULL DEFAULT 'TEXT',
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(255),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`group_id`) REFERENCES `group_chat`(`group_id`),
    FOREIGN KEY (`sender_id`) REFERENCES `user_account`(`user_id`)
);
-- ảnh của đoạn chat nhóm 
CREATE TABLE `group_message_attachment` (
    `attachment_id`  varchar(50) PRIMARY KEY ,
    `message_id` varchar(50),
    `attachment_url` VARCHAR(255) NOT NULL,
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(255),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`message_id`) REFERENCES `group_message`(`message_id`)
);
-- nhắn tin 1 - 1
CREATE TABLE `private_message` (
    `message_id`  varchar(50) PRIMARY KEY ,
    `sender_id` varchar(50),
    `receiver_id` varchar(50),
    `message_content` TEXT,
    `message_type` ENUM('TEXT', 'FILE') NOT NULL DEFAULT 'TEXT',
   `attachment_url` VARCHAR(255) ,
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(255),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`sender_id`) REFERENCES `user_account`(`user_id`),
    FOREIGN KEY (`receiver_id`) REFERENCES `user_account`(`user_id`)
);
-- ảnh trong đoạn chat riêng tư 
CREATE TABLE `private_message_attachment` (
    `attachment_id`  varchar(50) PRIMARY KEY ,
    `message_id` varchar(50),
    `attachment_url` VARCHAR(255) NOT NULL,
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(255),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`message_id`) REFERENCES `private_message`(`message_id`)
);

-- tin nổi bật
CREATE TABLE `highlight_story` (
    `story_id`  varchar(50) PRIMARY KEY ,
    `user_id` varchar(50),
    `story_name` VARCHAR(100) NOT NULL, 
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(255),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`user_id`) REFERENCES `user_account`(`user_id`)
);
-- các ảnh đi kèm trong bộ sưu tập nổi bật
CREATE TABLE `highlight_story_image` (
    `image_id`  varchar(50) PRIMARY KEY ,
    `story_id` varchar(50),  
    `image_url` VARCHAR(255) NOT NULL, 
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(255),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`story_id`) REFERENCES `highlight_story`(`story_id`) ON DELETE CASCADE
);





-- user_account
INSERT INTO `user_account` 
(`user_id`, `username`, `email`, `password`, `full_name`, `profile_picture_url`, `cover_picture_url`, `bio`, `hometown`, `date_of_birth`, `create_by`, `update_by`) 
VALUES
('1', 'nguyenvana', 'nguyenvana@gmail.com', 'hashed_password_1', 'Nguyễn Văn A', 'https://example.com/avatar1.jpg', 'https://example.com/cover1.jpg', 'Yêu công nghệ', 'Hà Nội', '1995-05-20', 'system', 'system'),
('2', 'tranthib', 'tranthib@gmail.com', 'hashed_password_2', 'Trần Thị B', 'https://example.com/avatar2.jpg', 'https://example.com/cover2.jpg', 'Thích du lịch', 'TP Hồ Chí Minh', '1998-12-10', 'system', 'system'),
('3', 'phamvanh', 'phamvanh@gmail.com', 'hashed_password_3', 'Phạm Văn H', 'https://example.com/avatar3.jpg', 'https://example.com/cover3.jpg', 'Lập trình viên', 'Đà Nẵng', '1990-07-15', 'system', 'system'),
('4', 'nguyenthic', 'nguyenthic@gmail.com', 'hashed_password_4', 'Nguyễn Thị C', 'https://example.com/avatar4.jpg', 'https://example.com/cover4.jpg', 'Yêu động vật', 'Cần Thơ', '1992-03-08', 'system', 'system'),
('5', 'lethid', 'lethid@gmail.com', 'hashed_password_5', 'Lê Thị D', 'https://example.com/avatar5.jpg', 'https://example.com/cover5.jpg', 'Thích nấu ăn', 'Hải Phòng', '1997-11-22', 'system', 'system');

-- security_token
INSERT INTO `security_token` 
(`token_id`, `user_id`, `access_token`, `expiration`, `created_at`) 
VALUES
('1', '1', 'access_token_1', '2024-12-31 23:59:59', CURRENT_TIMESTAMP),
('2', '2', 'access_token_2', '2024-12-31 23:59:59', CURRENT_TIMESTAMP),
('3', '3', 'access_token_3', '2024-12-31 23:59:59', CURRENT_TIMESTAMP),
('4', '4', 'access_token_4', '2024-12-31 23:59:59', CURRENT_TIMESTAMP),
('5', '5', 'access_token_5', '2024-12-31 23:59:59', CURRENT_TIMESTAMP);

-- post
INSERT INTO `post` 
(`post_id`, `user_id`, `content`, `privacy`, `create_by`, `update_by`) 
VALUES
('1', '1', 'Xin chào mọi người, hôm nay trời thật đẹp!', 'public', 'Nguyễn Văn A', 'Nguyễn Văn A'),
('2', '2', 'Đang chuẩn bị đi Đà Lạt, có ai đi cùng không?', 'friends', 'Trần Thị B', 'Trần Thị B'),
('3', '3', 'Một ngày bận rộn với các dòng code!', 'public', 'Phạm Văn H', 'Phạm Văn H'),
('4', '4', 'Tôi mới nhận nuôi một chú mèo con dễ thương!', 'public', 'Nguyễn Thị C', 'Nguyễn Thị C'),
('5', '5', 'Cuối tuần này mình định làm bánh, ai có công thức ngon chia sẻ nhé!', 'friends', 'Lê Thị D', 'Lê Thị D');

-- post_image
INSERT INTO `post_image` 
(`image_id`, `post_id`, `image_url`, `create_by`, `update_by`) 
VALUES
('1', '1', 'https://example.com/image1.jpg', 'Nguyễn Văn A', 'Nguyễn Văn A'),
('2', '2', 'https://example.com/image2.jpg', 'Trần Thị B', 'Trần Thị B'),
('3', '4', 'https://example.com/image3.jpg', 'Nguyễn Thị C', 'Nguyễn Thị C'),
('4', '5', 'https://example.com/image4.jpg', 'Lê Thị D', 'Lê Thị D');

-- post_reaction
INSERT INTO `post_reaction` 
(`reaction_id`, `post_id`, `user_id`, `reaction_type`, `create_by`, `update_by`) 
VALUES
('1', '1', '2', 'LIKE', 'Trần Thị B', 'Trần Thị B'),
('2', '3', '1', 'LOVE', 'Nguyễn Văn A', 'Nguyễn Văn A'),
('3', '5', '4', 'WOW', 'Nguyễn Thị C', 'Nguyễn Thị C');

-- Dữ liệu mẫu cho post_comment với phân cấp
INSERT INTO `post_comment` 
(`comment_id`, `post_id`, `user_id`, `content`, `parent_comment_id`, `create_by`, `update_by`) 
VALUES
-- Bình luận gốc (cấp 1)
('1', '1', '2', 'Bài viết hay quá!', NULL, 'Trần Thị B', 'Trần Thị B'),
('2', '1', '3', 'Hình ảnh đẹp quá!', NULL, 'Phạm Văn H', 'Phạm Văn H'),

-- Trả lời cho bình luận cấp 1 (cấp 2)
('3', '1', '4', 'Đồng ý với bạn, rất ấn tượng!', '1', 'Nguyễn Thị C', 'Nguyễn Thị C'),
('4', '1', '5', 'Chụp bằng máy gì thế nhỉ?', '2', 'Lê Thị D', 'Lê Thị D'),

-- Trả lời cho bình luận cấp 2 (cấp 3)
('5', '1', '5', 'Hình như chụp bằng máy DSLR!', '4', 'Lê Thị D', 'Lê Thị D'),
('6', '1', '5', 'Mình nghĩ là điện thoại flagship.', '4', 'Lê Thị D', 'Lê Thị D'),

-- Một chuỗi thảo luận khác (cấp 1, 2, 3)
('7', '2', '1', 'Mọi người có dự định đi đâu cuối tuần này?', NULL, 'Nguyễn Văn A', 'Nguyễn Văn A'),
('8', '2', '2', 'Chắc là đi biển, bạn thì sao?', '7', 'Trần Thị B', 'Trần Thị B'),
('9', '2', '3', 'Mình định leo núi.', '8', 'Phạm Văn H', 'Phạm Văn H'),
('10', '2', '4', 'Leo núi thú vị đấy, cho mình tham gia với!', '9', 'Nguyễn Thị C', 'Nguyễn Thị C');






-- friendship
INSERT INTO `friendship` 
(`friendship_id`, `user_id_1`, `user_id_2`, `status`, `create_by`, `update_by`) 
VALUES
('1', '1', '2', 'ACCEPTED', 'Nguyễn Văn A', 'Trần Thị B'),
('2', '2', '3', 'PENDING', 'Trần Thị B', 'Phạm Văn H'),
('3', '4', '5', 'ACCEPTED', 'Nguyễn Thị C', 'Lê Thị D');

-- group_chat
INSERT INTO `group_chat` 
(`group_id`, `group_name`, `group_image_url`, `admin_id`, `create_by`, `update_by`) 
VALUES
('1', 'Nhóm bạn thân', 'https://example.com/group1.jpg', '1', 'Nguyễn Văn A', 'Nguyễn Văn A'),
('2', 'Lập trình viên', 'https://example.com/group2.jpg', '3', 'Phạm Văn H', 'Phạm Văn H');

-- group_members
INSERT INTO `group_members` 
(`group_member_id`, `group_id`, `user_id`, `join_date`, `create_by`, `update_by`) 
VALUES
('1', '1', '1', CURRENT_TIMESTAMP, 'Nguyễn Văn A', 'Nguyễn Văn A'),
('2', '1', '2', CURRENT_TIMESTAMP, 'Nguyễn Văn A', 'Trần Thị B'),
('3', '2', '3', CURRENT_TIMESTAMP, 'Phạm Văn H', 'Phạm Văn H'),
('4', '2', '4', CURRENT_TIMESTAMP, 'Phạm Văn H', 'Nguyễn Thị C');

-- group_message
INSERT INTO `group_message` 
(`message_id`, `group_id`, `sender_id`, `message_content`, `message_type`, `create_by`, `update_by`) 
VALUES
('1', '1', '1', 'Chào mọi người!', 'TEXT', 'Nguyễn Văn A', 'Nguyễn Văn A'),
('2', '1', '2', 'Chào bạn!', 'TEXT', 'Trần Thị B', 'Trần Thị B'),
('3', '2', '3', 'Có ai đang làm project không?', 'TEXT', 'Phạm Văn H', 'Phạm Văn H');

-- group_message_attachment
INSERT INTO `group_message_attachment` 
(`attachment_id`, `message_id`, `attachment_url`, `create_by`, `update_by`) 
VALUES
('1', '3', 'https://example.com/attachment1.jpg', 'Phạm Văn H', 'Phạm Văn H');

-- private_message
INSERT INTO `private_message` 
(`message_id`, `sender_id`, `receiver_id`, `message_content`, `message_type`, `create_by`, `update_by`) 
VALUES
('1', '1', '2', 'Hôm nay thế nào?', 'TEXT', 'Nguyễn Văn A', 'Nguyễn Văn A'),
('2', '2', '3', 'Lâu quá không gặp!', 'TEXT', 'Trần Thị B', 'Trần Thị B'),
('3', '4', '5', 'Mình vừa gửi bạn tài liệu.', 'TEXT', 'Nguyễn Thị C', 'Nguyễn Thị C');

-- private_message_attachment
INSERT INTO `private_message_attachment` 
(`attachment_id`, `message_id`, `attachment_url`, `create_by`, `update_by`) 
VALUES
('1', '3', 'https://example.com/attachment2.jpg', 'Nguyễn Thị C', 'Nguyễn Thị C');

-- highlight_story
INSERT INTO `highlight_story` 
(`story_id`, `user_id`, `story_name`, `create_by`, `update_by`) 
VALUES
('1', '1', 'Kỷ niệm Hà Nội',  'Nguyễn Văn A', 'Nguyễn Văn A'),
('2', '5', 'Những món ăn yêu thích', 'Lê Thị D', 'Lê Thị D');

-- highlight_story_image
INSERT INTO `highlight_story_image` 
(`image_id`, `story_id`, `image_url`, `create_by`, `update_by`) 
VALUES
('1', '1', 'https://example.com/highlight1.jpg', 'Nguyễn Văn A', 'Nguyễn Văn A'),
('2', '2', 'https://example.com/highlight2.jpg', 'Lê Thị D', 'Lê Thị D');

