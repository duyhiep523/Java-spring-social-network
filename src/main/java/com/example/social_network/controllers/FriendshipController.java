package com.example.social_network.controllers;

import com.example.social_network.comon.enums.FriendshipStatus;
import com.example.social_network.dtos.Response.FriendReceivedResponse;
import com.example.social_network.dtos.Response.FriendRequestResponse;
import com.example.social_network.dtos.Response.FriendShipStatusUSent;
import com.example.social_network.entities.Friendship;
import com.example.social_network.response.Response;
import com.example.social_network.services.Iservice.IFriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/friendship")
@RequiredArgsConstructor
public class FriendshipController {

    private final IFriendshipService friendshipService;

    @PostMapping("/create")
    public ResponseEntity<?> createFriendship(@RequestParam String userId1, @RequestParam String userId2) {
        Friendship friendship = friendshipService.createFriendship(userId1, userId2);
        Response<Object> response = Response.builder()
                .message("Yêu cầu kết bạn được tạo thành công")
                .data(friendship)
                .success(true)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

        @PutMapping("/accept")
    public ResponseEntity<?> acceptFriendship(@RequestParam String userId1, @RequestParam String userId2) {
        Friendship friendship = friendshipService.acceptFriendship(userId1, userId2);
        Response<Object> response = Response.builder()
                .message("Yêu cầu kết bạn đã được chấp nhận")
                .data(friendship)
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/block")
    public ResponseEntity<?> blockFriendship(@RequestParam String userId1, @RequestParam String userId2) {
        Friendship friendship = friendshipService.blockFriendship(userId1, userId2);
        Response<Object> response = Response.builder()
                .message("Chặn thành công")
                .data(friendship)
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }
    @PutMapping("/unBlock")
    public ResponseEntity<?> unBlockFriendship(@RequestParam String userId1, @RequestParam String userId2) {
        Friendship friendship = friendshipService.unBlockFriendship(userId1, userId2);
        Response<Object> response = Response.builder()
                .message("Bỏ chặn thành công")
                .data(friendship)
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFriendship(@RequestParam String userId1, @RequestParam String userId2) {
        friendshipService.deleteFriendship(userId1, userId2);
        Response<Object> response = Response.builder()
                .message("Mối quan hệ bạn bè đã bị xoá")
                .data(null)
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<?> getFriendshipStatus(@RequestParam String userId1, @RequestParam String userId2) {
        FriendShipStatusUSent status = friendshipService.getFriendshipStatus(userId1, userId2);
        Response<Object> response = Response.builder()
                .message("Lấy trạng thái mối quan hệ thành công")
                .data(status)
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/received-requests")
    public ResponseEntity<?> getReceivedFriendRequests(@RequestParam String userId2) {
        List<FriendRequestResponse> friendRequests = friendshipService.getReceivedFriendRequests(userId2);

        Response<Object> response = Response.builder()
                .message("Lấy danh sách lời mời kết bạn thành công")
                .data(friendRequests)
                .success(true)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/{userId}")
    public ResponseEntity<?> countFriends(@PathVariable String userId) {
        long count = friendshipService.countFriends(userId);  // Gọi service để đếm số bạn bè
        Response<Object> response = Response.builder()
                .message("Đếm số bạn bè thành công")
                .data(count)
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/sent-requests")
    public ResponseEntity<?> getSentFriendRequests(@RequestParam String userId) {
        List<FriendReceivedResponse> sentRequests = friendshipService.getSentFriendRequests(userId);

        Response<Object> response = Response.builder()
                .message("Lấy danh sách lời mời kết bạn đã gửi thành công")
                .data(sentRequests)
                .success(true)
                .build();

        return ResponseEntity.ok(response);
    }


}
