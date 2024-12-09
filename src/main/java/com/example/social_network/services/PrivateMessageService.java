package com.example.social_network.services;

import com.example.social_network.dtos.Request.PrivateMessageDTO;
import com.example.social_network.dtos.Response.PrivateMessageResponse;
import com.example.social_network.entities.PrivateMessage;
import com.example.social_network.entities.User;
import com.example.social_network.exceptions.ResourceNotFoundException;
import com.example.social_network.repositories.PrivateMessageAttachmentRepository;
import com.example.social_network.repositories.PrivateMessageRepository;
import com.example.social_network.repositories.UserAccountRepository;
import com.example.social_network.services.Iservice.IPrivateMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateMessageService implements IPrivateMessageService {

    private final PrivateMessageRepository privateMessageRepository;
    private final com.example.social_network.services.CloudinaryService cloudinaryService;
    private final PrivateMessageAttachmentRepository privateMessageAttachmentRepository;
    private final UserAccountRepository userAccountRepository;

    @Override
    public PrivateMessageResponse createMessage(PrivateMessageDTO request) {
        User sender = userAccountRepository.findById(request.getSenderId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        User receiver = userAccountRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        // Tạo tin nhắn mới
        PrivateMessage message = new PrivateMessage();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessageContent(request.getMessageContent());
        message.setMessageType(request.getMessageType());

        // Xử lý tệp đính kèm nếu có
        if ("FILE".equals(request.getMessageType()) && request.getAttachments() != null && request.getAttachments().length > 0) {
            List<String> fileUrls = new ArrayList<>();
            for (MultipartFile file : request.getAttachments()) {
                String fileUrl = cloudinaryService.uploadImage(file); // Giả sử CloudinaryService dùng để upload file
                fileUrls.add(fileUrl);
            }
            message.setAttachmentUrl(String.join(",", fileUrls));
        }

        // Lưu tin nhắn vào cơ sở dữ liệu
        PrivateMessage savedMessage = privateMessageRepository.save(message);

        // Tạo và trả về đối tượng PrivateMessageResponse
        PrivateMessageResponse response = new PrivateMessageResponse(
                savedMessage.getMessageId(),
                new PrivateMessageResponse.SenderReceiverInfo(
                        sender.getUserId(),
                        sender.getFullName(),
                        sender.getProfilePictureUrl()
                ),
                new PrivateMessageResponse.SenderReceiverInfo(
                        receiver.getUserId(),
                        receiver.getFullName(),
                        receiver.getProfilePictureUrl()
                ),
                savedMessage.getMessageContent(),
                savedMessage.getMessageType(),
                savedMessage.getCreatedAt().toString()
        );

        return response;
    }
@Override
    public List<PrivateMessageResponse> getChatHistory(String senderId, String receiverId, int page, int size) {
        // Kiểm tra sự tồn tại của người gửi và người nhận
        User sender = userAccountRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        User receiver = userAccountRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));


        Pageable pageable =  PageRequest.of(page, size, Sort.by("createdAt").ascending());

        // Lấy lịch sử tin nhắn
        Page<PrivateMessage> messages = privateMessageRepository.findPrivateMessages(senderId, receiverId, pageable);


        List<PrivateMessageResponse> chatHistory = messages.stream().map(message -> {
            return new PrivateMessageResponse(
                    message.getMessageId(),
                    new PrivateMessageResponse.SenderReceiverInfo(senderId, sender.getFullName(), sender.getProfilePictureUrl()),  // Thông tin người gửi
                    new PrivateMessageResponse.SenderReceiverInfo(receiverId, receiver.getFullName(), receiver.getProfilePictureUrl()),  // Thông tin người nhận
                    message.getMessageContent(),
                    message.getMessageType(),
                    message.getCreatedAt().toString()
            );
        }).collect(Collectors.toList());

        return chatHistory;
    }


}
