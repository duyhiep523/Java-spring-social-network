package com.example.social_network.services;

import com.example.social_network.dtos.Request.AddGroupMembersRequest;
import com.example.social_network.dtos.Response.GroupMemberResponse;
import com.example.social_network.entities.GroupChat;
import com.example.social_network.entities.GroupMembers;
import com.example.social_network.entities.User;
import com.example.social_network.exceptions.InvalidParamException;
import com.example.social_network.exceptions.ResourceNotFoundException;
import com.example.social_network.repositories.GroupChatRepository;
import com.example.social_network.repositories.GroupMembersRepository;
import com.example.social_network.repositories.UserAccountRepository;
import com.example.social_network.services.Iservice.IGroupMembersService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupMembersService implements IGroupMembersService {
    private final GroupMembersRepository groupMembersRepository;
    private final GroupChatRepository groupChatRepository;
    private final UserAccountRepository userAccountRepository;

    @Override
    public void addGroupMember(String groupId, String userId) {
        GroupChat groupChat = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        User user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        boolean isMember = groupMembersRepository.existsByGroupChatAndUserAccount(groupChat, user);
        if (isMember) {
            throw new RuntimeException("User is already a member of the group");
        }
        GroupMembers newMember = new GroupMembers();
        newMember.setGroupChat(groupChat);
        newMember.setUserAccount(user);
        groupMembersRepository.save(newMember);
    }

    @Override
    public void addGroupMembers(AddGroupMembersRequest request) {
        GroupChat groupChat = groupChatRepository.findById(request.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        List<GroupMembers> newMembers = request.getUserIds().stream().map(userId -> {
            User user = userAccountRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));

            GroupMembers newMember = new GroupMembers();
            newMember.setGroupChat(groupChat);
            newMember.setUserAccount(user);
            return newMember;
        }).toList();
        groupMembersRepository.saveAll(newMembers);
    }

    @Override
    public void removeGroupMember(String adminId, String groupId, String userId) {
        GroupChat groupChat = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        Optional<GroupChat> groupChatA = groupChatRepository.findByGroupIdAndAdmin_UserId(groupId, adminId);
        if (groupChatA.isEmpty()) {
            throw new RuntimeException("Chỉ có admin của nhóm mới có thể xoá");
        }

        GroupMembers member = groupMembersRepository.findByGroupChat_GroupIdAndUserAccount_UserId(groupId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        groupMembersRepository.delete(member);
    }

    @Override
    public List<GroupMemberResponse> listGroupMembers(String groupId) {
        GroupChat groupChat = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        return groupMembersRepository.findByGroupChat(groupChat).stream()
                .map(groupMember -> {
                    User user = groupMember.getUserAccount();
                    String avatarUrl = user.getProfilePictureUrl();
                    String fullName = user.getFullName();
                    return new GroupMemberResponse(user.getUserId(), avatarUrl, fullName);
                })
                .collect(Collectors.toList());
    }


}
