package com.example.social_network.services;

import com.example.social_network.dtos.Request.GroupChatRequest;
import com.example.social_network.dtos.Response.GroupChatResponse;
import com.example.social_network.entities.GroupChat;
import com.example.social_network.entities.GroupMembers;
import com.example.social_network.entities.User;
import com.example.social_network.exceptions.ResourceNotFoundException;
import com.example.social_network.repositories.GroupChatRepository;
import com.example.social_network.repositories.GroupMembersRepository;
import com.example.social_network.repositories.GroupMessageRepository;
import com.example.social_network.repositories.UserAccountRepository;
import com.example.social_network.services.Iservice.IGroupChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupChatService implements IGroupChatService {
    private final GroupChatRepository groupChatRepository;
    private final GroupMembersRepository groupMembersRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final UserAccountRepository userAccountRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public GroupChatResponse createGroupChat(GroupChatRequest request) {
        User admin = userAccountRepository.findById(request.getAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        GroupChat groupChat = new GroupChat();
        groupChat.setGroupName(request.getGroupName());
        groupChat.setAdmin(admin);
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(request.getImage());
            groupChat.setGroupImageUrl(imageUrl);
        } else {
            throw new RuntimeException("ảnh đang bị trống");
        }

        GroupChat savedGroup = groupChatRepository.save(groupChat);

        GroupMembers adminMember = new GroupMembers();
        adminMember.setGroupChat(savedGroup);
        adminMember.setUserAccount(admin);
        groupMembersRepository.save(adminMember);

        return GroupChatResponse.builder()
                .groupId(savedGroup.getGroupId())
                .groupName(savedGroup.getGroupName())
                .adminId(savedGroup.getAdmin().getUserId())
                .image(savedGroup.getGroupImageUrl())
                .isDelete(savedGroup.getIsDeleted())
                .build();
    }
    @Override
    public GroupChatResponse updateGroupChat(String groupId, GroupChatRequest request) {

        GroupChat groupChat = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group chat not found"));

        if (request.getGroupName() != null && !request.getGroupName().isEmpty()) {
            groupChat.setGroupName(request.getGroupName());
        }

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadImage(request.getImage());
                groupChat.setGroupImageUrl(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Có lỗi khi tải ảnh lên: " + e.getMessage());
            }
        }

        GroupChat updatedGroup = groupChatRepository.save(groupChat);

        return GroupChatResponse.builder()
                .groupId(updatedGroup.getGroupId())
                .groupName(updatedGroup.getGroupName())
                .adminId(updatedGroup.getAdmin().getUserId())
                .image(updatedGroup.getGroupImageUrl())
                .isDelete(updatedGroup.getIsDeleted())
                .build();
    }


}
