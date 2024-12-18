package com.example.social_network.controllers;

import com.example.social_network.dtos.Request.PrivateMessageDTO;
import com.example.social_network.dtos.Response.PrivateMessageHistoryResponse;
import com.example.social_network.dtos.Response.PrivateMessageResponse;
import com.example.social_network.entities.PrivateMessage;
import com.example.social_network.response.Error;
import com.example.social_network.response.Response;
import com.example.social_network.services.Iservice.IPrivateMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/private-message")
@RequiredArgsConstructor
public class PrivateMessageController {
    private final IPrivateMessageService privateMessageService;
    private final SimpMessagingTemplate messagingTemplate;


    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @Valid @ModelAttribute PrivateMessageDTO request,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(new Error(errorMessages.toString(), "422"));
        }
        PrivateMessageResponse message = privateMessageService.createMessage(request);
        messagingTemplate.convertAndSendToUser(
                request.getReceiverId(), // Receiver ID (username or user ID)
                "/queue/private", // Destination for private messages
                message // Message content
        );

        System.out.println("Message Sent hihihiih: " + message);



        Response<Object> response = Response.builder()
                .message("Tin nhắn đã được gửi thành công")
                .data(message)
                .success(true)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getChatHistory(
            @NotBlank @RequestParam String senderId,
            @NotBlank @RequestParam String receiverId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {


        PrivateMessageHistoryResponse chatHistory = privateMessageService.getChatHistory(senderId, receiverId, page, size);


        Response<Object> response = Response.builder()
                .message("Lịch sử tin nhắn")
                .data(chatHistory)
                .success(true)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<?> deleteMessage(
            @PathVariable String messageId,
            @RequestParam String userId) {
        privateMessageService.deleteMessage(messageId, userId);

        Response<Object> response = Response.builder()
                .message("Tin nhắn đã được xóa thành công")
                .success(true)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
