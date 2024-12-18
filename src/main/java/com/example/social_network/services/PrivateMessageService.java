package com.example.social_network.services;

import com.example.social_network.dtos.Request.PrivateMessageDTO;
import com.example.social_network.dtos.Response.PrivateMessageHistoryResponse;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final SimpMessagingTemplate messagingTemplate;
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
        PrivateMessage savedMessage = privateMessageRepository.save(message);

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
                savedMessage.getAttachmentUrl(),
                savedMessage.getCreatedAt().toString(),
                savedMessage.getIsDeleted()
        );
        sendNotificationToReceiver(receiver.getUserId(), savedMessage);
        return response;
    }
    private void sendNotificationToReceiver(String receiverId, PrivateMessage message) {
        String destination = "/topic/notifications/" + receiverId;
        String notificationMessage = String.format("Bạn có tin nhắn mới từ %s: %s", message.getSender().getFullName(), message.getMessageContent());
        messagingTemplate.convertAndSendToUser(receiverId, destination, notificationMessage);
    }

    @Override
    public PrivateMessageHistoryResponse getChatHistory(String senderId, String receiverId, int page, int size) {

        User sender = userAccountRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        User receiver = userAccountRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("create_at").ascending());

        Page<PrivateMessage> messages = privateMessageRepository.findPrivateMessages(senderId, receiverId, pageable);


        List<PrivateMessageHistoryResponse.PrivateMessageResponse> chatHistory = messages.stream().map(message ->
                new PrivateMessageHistoryResponse.PrivateMessageResponse(
                        message.getMessageId(),
                        message.getMessageContent(),
                        message.getMessageType(),
                        message.getAttachmentUrl(),
                        message.getCreatedAt().toString(),
                        message.getIsDeleted(),
                        message.getSender().getUserId().equals(senderId)
                )
        ).collect(Collectors.toList());


        return new PrivateMessageHistoryResponse(
                new PrivateMessageHistoryResponse.SenderReceiverInfo(
                        senderId, sender.getFullName(), sender.getProfilePictureUrl()),
                new PrivateMessageHistoryResponse.SenderReceiverInfo(
                        receiverId, receiver.getFullName(), receiver.getProfilePictureUrl()),
                chatHistory,
                messages.getTotalElements(),
                messages.getTotalPages(),
                page,
                size
        );
    }

    @Override
    public void deleteMessage(String messageId, String userId) {
        PrivateMessage message = privateMessageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        if (!message.getSender().getUserId().equals(userId) && !message.getReceiver().getUserId().equals(userId)) {
            throw new SecurityException("You are not authorized to delete this message");
        }
        message.setIsDeleted(true);
        privateMessageRepository.save(message);
    }


}
