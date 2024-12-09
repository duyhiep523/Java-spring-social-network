package com.example.social_network.controllers;

import com.example.social_network.dtos.Request.PrivateMessageDTO;
import com.example.social_network.dtos.Response.PrivateMessageHistoryResponse;
import com.example.social_network.dtos.Response.PrivateMessageResponse;
import com.example.social_network.entities.PrivateMessage;
import com.example.social_network.response.Error;
import com.example.social_network.response.Response;
import com.example.social_network.services.Iservice.IPrivateMessageService;
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

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public String sendMessage(String message) {
        return message;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @Valid @ModelAttribute PrivateMessageDTO request,
            BindingResult result) {

        // Kiểm tra lỗi xác thực
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(new Error(errorMessages.toString(), "422"));
        }

        // Tạo tin nhắn
        PrivateMessageResponse message = privateMessageService.createMessage(request);

        // Gửi tin nhắn tới người nhận qua WebSocket
        messagingTemplate.convertAndSend(
                "/topic/messages/" + request.getReceiverId(),
                message
        );

        // Tạo phản hồi
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


}
