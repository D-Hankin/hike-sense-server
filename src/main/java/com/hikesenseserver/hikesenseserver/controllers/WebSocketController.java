package com.hikesenseserver.hikesenseserver.controllers;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.hikesenseserver.hikesenseserver.models.ChatMessage;
import com.hikesenseserver.hikesenseserver.models.FriendRequest;
import com.hikesenseserver.hikesenseserver.services.UserService;
import com.hikesenseserver.hikesenseserver.services.WebSocketService;

@Controller
public class WebSocketController {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private UserService userService;

    @MessageMapping("/friend-login")
    public void notifyFriendsOfOnlineStatus(Principal user) {
        System.out.println("Principal: " + user.getName() + " has logged in.");
        List<String> friendsUsernames = webSocketService.getFriendsUsernames(user.getName());
        userService.addUserOnline(user.getName());
        for (String friendUsername : friendsUsernames) {
            System.out.println("Sending message to " + friendUsername);
            messagingTemplate.convertAndSend("/topic/online-status/" + friendUsername, user.getName() + " is online.");
        }
    }

    @MessageMapping("/friend-logout")
    public void notifyFriendsOfOfflineStatus(Principal user) {
        List<String> friendsUsernames = webSocketService.getFriendsUsernames(user.getName());
        userService.removeUserOnline(user.getName().toString());
        for (String friendUsername : friendsUsernames) {
            messagingTemplate.convertAndSend("/topic/online-status/" + friendUsername, user.getName() + " is offline.");
        }
    }

    @MessageMapping("/friend-requests")
    public void handleFriendRequest(Principal user, @Payload FriendRequest request) {
        if (webSocketService.checkForUser(user, request.getReceiver()) && webSocketService.checkForExistingFriendRequest(request)) {
            messagingTemplate.convertAndSend("/topic/friend-requests/" + request.getReceiver(), 
                request.getSender() + " wants to be your friend.");
            userService.addFriendRequest(request);
        } else {
            messagingTemplate.convertAndSend("/topic/friend-requests/" + request.getSender(), 
                "The user " + request.getReceiver() + " is already your friend or does not exist.");
        }
    }

    @MessageMapping("/friend-requests/response")
    public void handleFriendRequestResponse(Principal user, @Payload FriendRequest request) {
    
        if ("ACCEPTED".equals(request.getStatus())) {
            // Accept the friend request and update the database
            webSocketService.acceptFriendRequest(request); // Update the database/service as necessary

            // Notify both users of the successful friend request acceptance
            messagingTemplate.convertAndSend("/topic/friend-requests/" + request.getReceiver(), 
                user.getName() + " has accepted your friend request."); // Notify the sender
            messagingTemplate.convertAndSend("/topic/friend-requests/" + request.getReceiver(), 
                user.getName() + " is now your friend."); // Notify the receiver
        } else {
            // Handle declined friend request
            messagingTemplate.convertAndSend("/topic/friend-requests/" + request.getReceiver(), 
                user.getName() + " has declined your friend request."); // Notify the sender
                webSocketService.removeFriendRequest(request); // Update the database/service as necessary
        }

    }

    @MessageMapping("/chat")
    public void handleChatMessage(Principal user, @Payload ChatMessage chatMessage) {
        System.out.println("Received chat message from " + user.getName() + " to " + chatMessage.getReceiver());
        // Assume chatMessage contains the sender, receiver, and message content
        chatMessage.setSender(user.getName());
        // webSocketService.saveChatMessage(chatMessage);  // Store the chat message in the database if needed

        // Send the message to the recipient's specific chat topic
        messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getSender(), chatMessage);
    }

}
