package com.hikesenseserver.hikesenseserver;

import static org.mockito.Mockito.*;

import java.security.Principal;

import com.hikesenseserver.hikesenseserver.controllers.WebSocketController;
import com.hikesenseserver.hikesenseserver.models.ChatMessage;
import com.hikesenseserver.hikesenseserver.models.FriendRequest;
import com.hikesenseserver.hikesenseserver.services.UserService;
import com.hikesenseserver.hikesenseserver.services.WebSocketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

class WebSocketControllerTest {

    @InjectMocks
    private WebSocketController webSocketController;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private WebSocketService webSocketService;

    @Mock
    private UserService userService;

    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        principal = () -> "username";
    }

    @Test
    void testNotifyFriendsOfOnlineStatus() {
        webSocketController.notifyFriendsOfOnlineStatus(principal);
        verify(userService).addUserOnline("username");
        verify(messagingTemplate).convertAndSend("/topic/online-status/username", "username is online.");
    }

    @Test
    void testHandleFriendRequest() {
        FriendRequest request = new FriendRequest("sender@example.com", "receiver@example.com", "PENDING");
        when(webSocketService.checkForUser(principal, "receiver@example.com")).thenReturn(true);
        when(webSocketService.checkForExistingFriendRequest(request)).thenReturn(true);

        webSocketController.handleFriendRequest(principal, request);

        verify(messagingTemplate).convertAndSend("/topic/friend-requests/receiver@example.com", "sender@example.com wants to be your friend.");
        verify(userService).addFriendRequest(request);
    }

    @Test
    void testHandleChatMessage() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setReceiver("receiver@example.com");
        
        webSocketController.handleChatMessage(principal, chatMessage);

        verify(messagingTemplate).convertAndSend("/topic/chat/username", chatMessage);
    }
    
    @Test
    void testHandleFriendRequestResponseAccepted() {
        FriendRequest request = new FriendRequest("sender@example.com", "receiver@example.com", "ACCEPTED");

        webSocketController.handleFriendRequestResponse(principal, request);

        verify(webSocketService).acceptFriendRequest(request);
        verify(messagingTemplate).convertAndSend("/topic/friend-requests/receiver@example.com", "username has accepted your friend request.");
    }

    @Test
    void testHandleChatNotificationResponse() {
        String recipient = "recipient@example.com";
        
        webSocketController.notifyChatRecipient(principal, recipient);
        
        verify(messagingTemplate).convertAndSend("/topic/chat/notify/username", "username");
    }
}

