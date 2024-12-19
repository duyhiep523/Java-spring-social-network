package com.example.social_network.services;

import com.example.social_network.dtos.Request.GroupMessageRequest;
import com.example.social_network.dtos.Response.GroupChatUserResponse;
import com.example.social_network.dtos.Response.GroupMessageHistoryRespone;
import com.example.social_network.dtos.Response.GroupMessageResponse;
import com.example.social_network.dtos.Response.LastMessageResponse;
import com.example.social_network.entities.GroupChat;
import com.example.social_network.entities.GroupMembers;
import com.example.social_network.entities.GroupMessage;
import com.example.social_network.entities.User;
import com.example.social_network.exceptions.ResourceNotFoundException;
import com.example.social_network.repositories.*;
import com.example.social_network.services.Iservice.IGroupMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupMessageService implements IGroupMessageService {
    private final GroupMessageRepository groupMessageRepository;
    private final GroupChatRepository groupChatRepository;
    private final com.example.social_network.services.CloudinaryService cloudinaryService;
    private final UserAccountRepository userAccountRepository;
    private final GroupMembersRepository groupMembersRepository;

    @Override
    public GroupMessageResponse createMessage(String groupId, GroupMessageRequest request) {
        User sender = userAccountRepository.findById(request.getSenderId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));

        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Nhóm chat không tồn tại"));

        GroupMessage message = new GroupMessage();
        message.setSender(sender);
        message.setGroupChat(group);
        message.setMessageContent(request.getMessageContent());
        message.setMessageType(request.getMessageType());
        message.setIsDeleted(false);
        if ("FILE".equals(request.getMessageType()) && request.getAttachments() != null && request.getAttachments().length > 0) {
            List<String> fileUrls = new ArrayList<>();
            for (MultipartFile file : request.getAttachments()) {
                String fileUrl = cloudinaryService.uploadImage(file);
                fileUrls.add(fileUrl);
            }
            message.setAttachmentUrl(String.join(",", fileUrls));
        }


        GroupMessage savedMessage = groupMessageRepository.save(message);
        List<GroupMembers> groupMembers = groupMembersRepository.findMembersByGroupId(groupId);
        for (GroupMembers member : groupMembers) {
            if (!member.getUserAccount().getUserId().equals(sender.getUserId())) {
                // Gửi thông báo cho từng thành viên trong nhóm (trừ người gửi)
                sendNotificationToUser(member.getUserAccount(), savedMessage);
            }
        }
        GroupMembers members = groupMembersRepository.findByGroupChat_GroupIdAndUserAccount_UserId(groupId, request.getSenderId()).orElseThrow(() -> new ResourceNotFoundException("không tìm thấy thành viên trong nhóm"));
        String nickName = members.getNickName();

        return new GroupMessageResponse(
                savedMessage.getMessageId(),
                new GroupMessageResponse.SenderReceiverInfo(
                        sender.getUserId(),
                        sender.getFullName(),
                        sender.getProfilePictureUrl(),
                        nickName
                ),
                savedMessage.getMessageContent(),
                savedMessage.getMessageType(),
                savedMessage.getAttachmentUrl(),
                savedMessage.getIsDeleted(),
                savedMessage.getCreatedAt().toString()
        );
    }

    private void sendNotificationToUser(User user, GroupMessage message) {

        String notificationMessage = "Bạn có một tin nhắn mới trong nhóm: " + message.getMessageContent();

    }

    @Override
    public void deleteMessage(String messageId, String userId) {
        GroupMessage message = groupMessageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        if (!message.getSender().getUserId().equals(userId)) {
            throw new SecurityException("You are not authorized to delete this message");
        }
        message.setIsDeleted(true);
        groupMessageRepository.save(message);
    }


    @Override
    public GroupMessageHistoryRespone getGroupMessageHistory(String groupId, int page, int size) {

        GroupChat groupChat = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));


        Page<GroupMessage> groupMessages = groupMessageRepository.findByGroupChat(groupChat, pageable);

        Long totalElements = groupMessages.getTotalElements();
        int totalPages = groupMessages.getTotalPages();

        List<GroupMessageHistoryRespone.GroupMessageResponse> messages = groupMessages.stream()
                .map(message -> {
                    User sender = message.getSender();
                    GroupMembers members = groupMembersRepository.findByGroupChat_GroupIdAndUserAccount_UserId(groupId, sender.getUserId()).orElseThrow(() -> new ResourceNotFoundException("không tìm thấy thành viên trong nhóm"));
                    String nickName = members.getNickName();
                    GroupMessageHistoryRespone.SenderReceiverInfo senderInfo = new GroupMessageHistoryRespone.SenderReceiverInfo(
                            sender.getUserId(),
                            sender.getFullName(),
                            sender.getProfilePictureUrl(),
                            nickName
                    );
                    return new GroupMessageHistoryRespone.GroupMessageResponse(
                            message.getMessageId(),
                            senderInfo,
                            message.getMessageContent(),
                            message.getMessageType(),
                            message.getAttachmentUrl(),
                            message.getIsDeleted(),
                            message.getCreatedAt().toString()
                    );
                })
                .collect(Collectors.toList());

        return new GroupMessageHistoryRespone(
                groupId,
                messages,
                totalElements,
                totalPages,
                page,
                size
        );
    }


    @Override
    public List<GroupChatUserResponse> getGroupChatsByUser(String userId) {
        List<Object[]> results = groupChatRepository.findGroupChatInfoByUserId(userId);

        List<GroupChatUserResponse> groupChatInfoList = new ArrayList<>();
        for (Object[] result : results) {
            String groupId = (String) result[0];
            String groupName = (String) result[1];
            String groupImageUrl = (String) result[2];

            GroupChatUserResponse groupChatInfo = GroupChatUserResponse.builder()
                    .groupId(groupId)
                    .groupName(groupName)
                    .groupImageUrl(groupImageUrl)
                    .lastMessageResponse(getLastMessageByGroupId(groupId))
                    .build();
            groupChatInfoList.add(groupChatInfo);
        }

        return groupChatInfoList;
    }


    public LastMessageResponse getLastMessageByGroupId(String groupId) {
        Optional<Object[]> result = groupMessageRepository.findLastMessageByGroupId(groupId);
        if (result.isPresent()) {
            Object[] outerData = result.get();
            if (outerData.length > 0 && outerData[0] instanceof Object[] data) {
                if (data.length >= 4) {
                    return LastMessageResponse.builder()
                            .messageId(String.valueOf(data[0]))
                            .senderId(String.valueOf(data[1]))
                            .senderName(String.valueOf(data[2]))
                            .messageContent(String.valueOf(data[3]))
                            .createAt(String.valueOf(data[4]))
                            .build();
                }
            }
            throw new IllegalArgumentException("Dữ liệu trả về không đúng định dạng.");
        }
        throw new ResourceNotFoundException("Không tìm thấy tin nhắn cuối cùng trong nhóm.");
    }


}
