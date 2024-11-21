package com.example.social_network.controllers;

import com.example.social_network.comon.enums.FriendshipStatus;
import com.example.social_network.entities.Friendship;
import com.example.social_network.services.FriendshipService;
import com.example.social_network.services.Iservice.IFriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${apiPrefix}/friendship")
@RequiredArgsConstructor
public class FriendshipController {

    @Autowired
    private IFriendshipService friendshipService;

    @PostMapping("/create")
    public Friendship createFriendship(
            @RequestParam String userId1,
            @RequestParam String userId2) {
        return friendshipService.createFriendship(userId1, userId2);
    }

    @PostMapping("/accept")
    public Friendship acceptFriendship(
            @RequestParam String userId1,
            @RequestParam String userId2) {
        return friendshipService.acceptFriendship(userId1, userId2);
    }

    @PostMapping("/reject")
    public Friendship rejectFriendship(@RequestParam String userId1, @RequestParam String userId2) {
        return friendshipService.rejectFriendship(userId1, userId2);
    }

    @PostMapping("/delete")
    public void deleteFriendship(@RequestParam String userId1, @RequestParam String userId2) {
        friendshipService.deleteFriendship(userId1, userId2);
    }

    @GetMapping("/status")
    public FriendshipStatus getFriendshipStatus(@RequestParam String userId1, @RequestParam String userId2) {
        return friendshipService.getFriendshipStatus(userId1, userId2);
    }
}
