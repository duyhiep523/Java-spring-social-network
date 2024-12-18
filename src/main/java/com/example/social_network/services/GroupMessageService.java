package com.example.social_network.services;

import com.example.social_network.dtos.Request.GroupMessageRequest;
import com.example.social_network.dtos.Response.GroupChatResponse;
import com.example.social_network.dtos.Response.GroupMessageResponse;
import com.example.social_network.dtos.Response.PrivateMessageResponse;
import com.example.social_network.entities.GroupChat;
import com.example.social_network.entities.GroupMessage;
import com.example.social_network.entities.PrivateMessage;
import com.example.social_network.entities.User;
import com.example.social_network.exceptions.ResourceNotFoundException;
import com.example.social_network.repositories.*;
import com.example.social_network.services.Iservice.IGroupMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMessageService implements IGroupMessageService {
    private final GroupMessageRepository groupMessageRepository;
    private final GroupChatRepository groupChatRepository;
    private final com.example.social_network.services.CloudinaryService cloudinaryService;
    private final UserAccountRepository userAccountRepository;

    @Override
    public GroupMessageResponse createMessage(String groupId, GroupMessageRequest request) {
        User sender = userAccountRepository.findById(request.getSenderId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));

        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Nhóm chat không tồn tại"));
        // Tạo tin nhắn mới
        GroupMessage message = new GroupMessage();
        message.setSender(sender);
        message.setGroupChat(group);
        message.setMessageContent(request.getMessageContent());
        message.setMessageType(request.getMessageType());
        message.setIsDeleted(false);
        // Xử lý tệp đính kèm nếu có
        if ("FILE".equals(request.getMessageType()) && request.getAttachments() != null && request.getAttachments().length > 0) {
            List<String> fileUrls = new ArrayList<>();
            for (MultipartFile file : request.getAttachments()) {
                String fileUrl = cloudinaryService.uploadImage(file);
                fileUrls.add(fileUrl);
            }
            message.setAttachmentUrl(String.join(",", fileUrls));
        }

        // Lưu tin nhắn vào cơ sở dữ liệu
        GroupMessage savedMessage = groupMessageRepository.save(message);

        // Tạo và trả về đối tượng PrivateMessageResponse

        return new GroupMessageResponse(
                savedMessage.getMessageId(),
                new GroupMessageResponse.SenderReceiverInfo(
                        sender.getUserId(),
                        sender.getFullName(),
                        sender.getProfilePictureUrl()
                ),
                savedMessage.getMessageContent(),
                savedMessage.getMessageType(),
                savedMessage.getAttachmentUrl(),
                savedMessage.getIsDeleted(),
                savedMessage.getCreatedAt().toString()
        );
    }
}
