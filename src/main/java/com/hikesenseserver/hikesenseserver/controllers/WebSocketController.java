package com.hikesenseserver.hikesenseserver.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.hikesenseserver.hikesenseserver.models.ChatMessage;
import com.hikesenseserver.hikesenseserver.models.FriendRequest;
import com.hikesenseserver.hikesenseserver.models.NotificationResponse;
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
        System.out.println("Principal: " + user.getName() + " is logging in.");
        System.out.println("name type: " + user.getName().getClass().getName());
        userService.addUserOnline(user.getName());
        System.out.println("/topic/online-status/" + user.getName().trim() + ", " + user.getName() + " is online.");
        messagingTemplate.convertAndSend("/topic/online-status/" + user.getName().trim(), user.getName() + " is online.");
    }

    @MessageMapping("/friend-logout")
    public void notifyFriendsOfOfflineStatus(Principal user) {
        System.out.println("Principal: " + user.getName() + " is logging out.");
        userService.removeUserOnline(user.getName().toString());    
        messagingTemplate.convertAndSend("/topic/online-status/" + user.getName().trim(), user.getName() + " is offline.");
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
            webSocketService.acceptFriendRequest(request); 
            messagingTemplate.convertAndSend("/topic/friend-requests/" + request.getReceiver(), 
                user.getName() + " has accepted your friend request."); 
            messagingTemplate.convertAndSend("/topic/friend-requests/" + request.getReceiver(), 
                user.getName() + " is now your friend."); 
        } else {
            messagingTemplate.convertAndSend("/topic/friend-requests/" + request.getReceiver(), 
                user.getName() + " has declined your friend request."); 
                webSocketService.removeFriendRequest(request); 
        }

    }

    @MessageMapping("/chat")
    public void handleChatMessage(Principal user, @Payload ChatMessage chatMessage) {
        System.out.println("Received chat message from " + user.getName() + " to " + chatMessage.getReceiver());
        chatMessage.setSender(user.getName());
        messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getSender(), chatMessage);
    }

    @MessageMapping("/chat/notify")
    public void notifyChatRecipient(Principal user, @Payload String recipient) {
        System.out.println("Received chat notification from " + user.getName() + " to " + recipient);
        messagingTemplate.convertAndSend("/topic/chat/notify/" + user.getName(), user.getName());
    }

    @MessageMapping("/chat/notification-response")
    public void handleChatNotificationResponse(Principal user, @Payload NotificationResponse notificationResponse) {
        System.out.println("Received chat notification response from " + user.getName() + " to " + notificationResponse.getRecipient());   
        messagingTemplate.convertAndSend("/topic/chat/notification-response/" + user.getName(), notificationResponse.getResponse());
    }

}
