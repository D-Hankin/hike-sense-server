package com.hikesenseserver.hikesenseserver.controllers;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.hikesenseserver.hikesenseserver.models.FriendRequest;
import com.hikesenseserver.hikesenseserver.services.WebSocketService;

public class WebSocketController {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private WebSocketService webSocketService;

    @MessageMapping("/friend-login")
    public void notifyFriendsOfOnlineStatus(Principal user) {
        List<String> friendsUsernames = webSocketService.getFriendsUsernames(user.getName());
        for (String friendUsername : friendsUsernames) {
            messagingTemplate.convertAndSend("/topic/online-status/" + friendUsername, user.getName() + " is online.");
        }
    }

    @MessageMapping("/friend-logout")
    public void notifyFriendsOfOfflineStatus(Principal user) {
        List<String> friendsUsernames = webSocketService.getFriendsUsernames(user.getName());
        for (String friendUsername : friendsUsernames) {
            messagingTemplate.convertAndSend("/topic/online-status/" + friendUsername, user.getName() + " is offline.");
        }
    }

    @MessageMapping("/friend-requests")
    public void handleFriendRequest(Principal user, FriendRequest request) {
        // Assuming FriendRequest has sender and receiver fields
        webSocketService.sendFriendRequest(request);
        messagingTemplate.convertAndSend("/topic/friend-requests/" + request.getReceiver(), 
            request.getSender() + " wants to be your friend.");
    }

    @MessageMapping("/friend-requests/response")
    public void handleFriendRequestResponse(Principal user, FriendRequest request) {
        if ("ACCEPTED".equals(request.getStatus())) {
            webSocketService.acceptFriendRequest(request); // Update the database/service as necessary
            messagingTemplate.convertAndSend("/topic/friend-requests/" + request.getSender(), 
                user.getName() + " accepted your friend request.");
        } else {
            webSocketService.declineFriendRequest(request); // Update the database/service as necessary
            messagingTemplate.convertAndSend("/topic/friend-requests/" + request.getSender(), 
                user.getName() + " declined your friend request.");
        }
    }


}
