DROP DATABASE IF EXISTS social_media_db;
CREATE DATABASE social_media_db;
USE social_media_db;
-- bang nguoi dung he thong 
CREATE TABLE `user_account` (
    `user_id` INT PRIMARY KEY AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `full_name` VARCHAR(100) NOT NULL,
    `profile_picture_url` VARCHAR(255) DEFAULT NULL,
    `cover_picture_url` VARCHAR(255) DEFAULT NULL,
    `bio` TEXT DEFAULT NULL,
    `hometown` VARCHAR(255) DEFAULT NULL, 
    `date_of_birth` DATE DEFAULT NULL, 
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` VARCHAR(500),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` VARCHAR(500),
    `is_deleted` BOOLEAN DEFAULT FALSE
);

-- refreshtoken
CREATE TABLE `security_token` (
    `token_id` INT PRIMARY KEY AUTO_INCREMENT,
    `user_id` INT,
    `access_token` VARCHAR(255) NOT NULL,
    `expiration` DATETIME,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user_account`(`user_id`)
);

-- bang bai viet chung
CREATE TABLE `post` (
    `post_id` INT PRIMARY KEY AUTO_INCREMENT,
    `user_id` INT,
    `content` TEXT,
    `privacy` ENUM('public', 'private', 'friends') DEFAULT 'public',  
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` VARCHAR(500),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` VARCHAR(500),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`user_id`) REFERENCES `user_account`(`user_id`)
);

-- lưu thông tin về file phương tiện của bài viết
CREATE TABLE `post_image` (
    `image_id` INT PRIMARY KEY AUTO_INCREMENT,
    `post_id` INT,
    `image_url` VARCHAR(255) NOT NULL,
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(500),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(500),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`post_id`) REFERENCES `post`(`post_id`)
);
-- thả cảm xúc 
CREATE TABLE `post_reaction` (
    `reaction_id` INT PRIMARY KEY AUTO_INCREMENT,
    `post_id` INT,
    `user_id` INT,
    `reaction_type` ENUM('LIKE', 'LOVE', 'HAHA', 'WOW', 'SAD', 'ANGRY') NOT NULL,
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(500),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(500),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`post_id`) REFERENCES `post`(`post_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user_account`(`user_id`)
);

-- comment về bài viết
CREATE TABLE `post_comment` (
    `comment_id` INT PRIMARY KEY AUTO_INCREMENT,
    `post_id` INT,                  
    `user_id` INT,                
    `content` TEXT NOT NULL,         
    `parent_comment_id` INT DEFAULT NULL,  
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP, 
    `create_by` varchar(500), 
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  
    `update_by` varchar(500),
    `is_deleted` BOOLEAN DEFAULT FALSE, 
    FOREIGN KEY (`post_id`) REFERENCES `post`(`post_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user_account`(`user_id`),
    FOREIGN KEY (`parent_comment_id`) REFERENCES `post_comment`(`comment_id`)  
);
-- quan hệ bạn bè 
CREATE TABLE `friendship` (
    `friendship_id` INT PRIMARY KEY AUTO_INCREMENT,
    `user_id_1` INT,
    `user_id_2` INT,
    `status` ENUM('PENDING', 'ACCEPTED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(500),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(500),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`user_id_1`) REFERENCES `user_account`(`user_id`),
    FOREIGN KEY (`user_id_2`) REFERENCES `user_account`(`user_id`)
);
-- nhóm chat 
CREATE TABLE `group_chat` (
    `group_id` INT PRIMARY KEY AUTO_INCREMENT,
    `group_name` VARCHAR(100) NOT NULL,
    `group_image_url` VARCHAR(255) DEFAULT NULL,
    `admin_id` INT,
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(500),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(500),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`admin_id`) REFERENCES `user_account`(`user_id`)
);
-- thành viên trong đoạn chat
CREATE TABLE `group_members` (
    `group_member_id` INT PRIMARY KEY AUTO_INCREMENT,
    `group_id` INT,
    `user_id` INT,
    `join_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(500),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(500),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`group_id`) REFERENCES `group_chat`(`group_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user_account`(`user_id`)
);
CREATE TABLE `group_message` (
    `message_id` INT PRIMARY KEY AUTO_INCREMENT,
    `group_id` INT,
    `sender_id` INT,
    `message_content` TEXT,
    `message_type` ENUM('TEXT', 'IMAGE') NOT NULL DEFAULT 'TEXT',
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(500),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(500),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`group_id`) REFERENCES `group_chat`(`group_id`),
    FOREIGN KEY (`sender_id`) REFERENCES `user_account`(`user_id`)
);
-- ảnh của đoạn chat nhóm 
CREATE TABLE `group_message_attachment` (
    `attachment_id` INT PRIMARY KEY AUTO_INCREMENT,
    `message_id` INT,
    `attachment_url` VARCHAR(255) NOT NULL,
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(500),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(500),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`message_id`) REFERENCES `group_message`(`message_id`)
);
-- nhắn tin 1 - 1
CREATE TABLE `private_message` (
    `message_id` INT PRIMARY KEY AUTO_INCREMENT,
    `sender_id` INT,
    `receiver_id` INT,
    `message_content` TEXT,
    `message_type` ENUM('TEXT', 'IMAGE') NOT NULL DEFAULT 'TEXT',
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(500),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(500),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`sender_id`) REFERENCES `user_account`(`user_id`),
    FOREIGN KEY (`receiver_id`) REFERENCES `user_account`(`user_id`)
);
-- ảnh trong đoạn chat riêng tư 
CREATE TABLE `private_message_attachment` (
    `attachment_id` INT PRIMARY KEY AUTO_INCREMENT,
    `message_id` INT,
    `attachment_url` VARCHAR(255) NOT NULL,
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` varchar(500),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` varchar(500),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`message_id`) REFERENCES `private_message`(`message_id`)
);

-- tin nổi bật
CREATE TABLE `highlight_story` (
    `story_id` INT PRIMARY KEY AUTO_INCREMENT,
    `user_id` INT,
    `title` VARCHAR(100) NOT NULL,  
    `description` TEXT DEFAULT NULL,  
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` VARCHAR(500),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` VARCHAR(500),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`user_id`) REFERENCES `user_account`(`user_id`)
);
-- các ảnh đi kèm trong bộ sưu tập nổi bật
CREATE TABLE `highlight_story_image` (
    `image_id` INT PRIMARY KEY AUTO_INCREMENT,
    `story_id` INT,  
    `image_url` VARCHAR(255) NOT NULL, 
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `create_by` VARCHAR(500),
    `update_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by` VARCHAR(500),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`story_id`) REFERENCES `highlight_story`(`story_id`) ON DELETE CASCADE
);






