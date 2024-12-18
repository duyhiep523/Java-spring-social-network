package com.example.social_network.controllers;

import com.example.social_network.dtos.Request.AddGroupMembersRequest;
import com.example.social_network.dtos.Request.GroupChatRequest;
import com.example.social_network.dtos.Request.GroupMessageRequest;
import com.example.social_network.dtos.Response.*;
import com.example.social_network.response.Error;
import com.example.social_network.response.Response;
import com.example.social_network.services.Iservice.IGroupChatService;
import com.example.social_network.services.Iservice.IGroupMembersService;
import com.example.social_network.services.Iservice.IGroupMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/groupChat")
@RequiredArgsConstructor
public class GroupMessageController {
    private final IGroupMembersService groupMembersService;
    private final IGroupChatService groupChatService;
    private final IGroupMessageService groupMessageService;

    // tạo mới nhóm chat
    @PostMapping("/create")
    public ResponseEntity<?> createGroupChat(@Valid @ModelAttribute GroupChatRequest request, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String error = errorMessages.toString();
            Error errorResponse = new Error(error, "422");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        GroupChatResponse groupChatResponse = groupChatService.createGroupChat(request);

        Response<GroupChatResponse> response = Response.<GroupChatResponse>builder()
                .message("Nhóm chat đã được tạo thành công")
                .data(groupChatResponse)
                .success(true)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // cập nhật nhóm chat
    @PutMapping("/update/{groupId}")
    public ResponseEntity<?> updateGroupChat(@PathVariable String groupId,
                                             @Valid @ModelAttribute GroupChatRequest request, BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String error = errorMessages.toString();
            Error errorResponse = new Error(error, "422");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        GroupChatResponse updatedGroupChat = groupChatService.updateGroupChat(groupId, request);
        Response<GroupChatResponse> response = Response.<GroupChatResponse>builder()
                .message("Thông tin nhóm đã được cập nhật thành công")
                .data(updatedGroupChat)
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }

    // thêm thành viên vào nhóm
    @PostMapping("add-member")
    public ResponseEntity<?> addGroupMember(
            @RequestParam String groupId,
            @RequestParam String userId) {
        groupMembersService.addGroupMember(groupId, userId);
        Response<Object> response = Response.builder()
                .message("Thành viên đã được thêm vào nhóm thành công")
                .success(true)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // thêm nhiều thành viên () skip mẹ đi
    @PostMapping("add-members")
    public ResponseEntity<?> addGroupMembers(
            @Valid @RequestBody AddGroupMembersRequest request, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            String error = errorMessages.toString();
            Error errorResponse = new Error(error, "422");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        groupMembersService.addGroupMembers(request);
        Response<Object> response = Response.builder()
                .message("Các thành viên đã được thêm vào nhóm thành công")
                .success(true)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // xoá khoi nhom chat
    @DeleteMapping("remove-member")
    public ResponseEntity<?> removeGroupMember(
            @RequestParam String adminId,
            @RequestParam String groupId,
            @RequestParam String userId) {
        groupMembersService.removeGroupMember(adminId, groupId, userId);
        Response<Object> response = Response.builder()
                .message("Thành viên đã được xóa khỏi nhóm thành công")
                .success(true)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // danh sách thành viên trong nhoms chat
    @GetMapping("list-members/{groupId}")
    public ResponseEntity<?> listGroupMembers(@PathVariable String groupId) {
        List<?> members = groupMembersService.listGroupMembers(groupId);
        Response<List<?>> response = Response.<List<?>>builder()
                .message("Danh sách các thành viên trong nhóm")
                .data(members)
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }


    // gửi tin nhắn vào nhóm chat
    @PostMapping("{groupId}/messages/send")
    public ResponseEntity<?> createGroupMessage(@PathVariable String groupId,
                                                @Valid @ModelAttribute GroupMessageRequest request) {

        GroupMessageResponse responseMessage = groupMessageService.createMessage(groupId, request);


        Response<GroupMessageResponse> response = Response.<GroupMessageResponse>builder()
                .message("Tin nhắn đã được gửi thành công")
                .data(responseMessage)
                .success(true)
                .build();

        return ResponseEntity.ok(response);
    }


    // API để xóa tin nhắn
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable String messageId,
                                           @RequestParam String userId) {
        groupMessageService.deleteMessage(messageId, userId);

        Response<String> response = Response.<String>builder()
                .message("Tin nhắn đã được xóa thành công")
                .data("Message ID: " + messageId)
                .success(true)
                .build();

        return ResponseEntity.ok(response);

    }

    @GetMapping("{groupId}/messages/history")
    public ResponseEntity<?> getGroupMessageHistory(
            @PathVariable String groupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

            GroupMessageHistoryRespone response = groupMessageService.getGroupMessageHistory(groupId, page, size);

            Response<GroupMessageHistoryRespone> result = Response.<GroupMessageHistoryRespone>builder()
                    .message("Lịch sử tin nhắn của nhóm")
                    .data(response)
                    .success(true)
                    .build();

            return ResponseEntity.ok(result);

    }
    // Lấy danh sách nhóm chat của người dùng
    @GetMapping("/user/{userId}/groups")
    public ResponseEntity<?> getGroupChatsByUser(@PathVariable String userId) {
        List<GroupChatUserResponse> groupChats = groupMessageService.getGroupChatsByUser(userId);

        Response<List<GroupChatUserResponse>> response = Response.<List<GroupChatUserResponse>>builder()
                .message("Danh sách nhóm chat của người dùng")
                .data(groupChats)
                .success(true)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/groups/{groupId}/last-message")
    public ResponseEntity<?> getLastMessage(@PathVariable String groupId) {
        LastMessageResponse lastMessage = groupMessageService.getLastMessageByGroupId(groupId);

        Response<LastMessageResponse> response = Response.<LastMessageResponse>builder()
                .message("Tin nhắn cuối cùng trong nhóm")
                .data(lastMessage)
                .success(true)
                .build();

        return ResponseEntity.ok(response);
    }

}
