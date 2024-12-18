package com.example.social_network.controllers;

import com.example.social_network.dtos.Request.AddGroupMembersRequest;
import com.example.social_network.dtos.Request.GroupChatRequest;
import com.example.social_network.dtos.Response.GroupChatResponse;
import com.example.social_network.response.Error;
import com.example.social_network.response.Response;
import com.example.social_network.services.Iservice.IGroupChatService;
import com.example.social_network.services.Iservice.IGroupMembersService;
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

    @PutMapping("/update/{groupId}")
    public ResponseEntity<?> updateGroupChat(@PathVariable String groupId,
                                             @Valid @ModelAttribute GroupChatRequest request , BindingResult result
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
}
