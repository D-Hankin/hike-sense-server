package com.hikesenseserver.hikesenseserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import com.hikesenseserver.hikesenseserver.models.Friend;
import com.hikesenseserver.hikesenseserver.models.FriendRequest;
import com.hikesenseserver.hikesenseserver.models.User;
import com.hikesenseserver.hikesenseserver.repositories.UserRepository;
import com.hikesenseserver.hikesenseserver.services.WebSocketService;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

class WebSocketServiceTest {

    @InjectMocks
    private WebSocketService webSocketService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setFriends(new ArrayList<>());
        user.setPendingFriendRequests(new ArrayList<>());
        user.setSubscriptionStatus("free");
        user.setFriends(Collections.singletonList(new Friend("friend@example.com", "Friend", "User", null)));
    }

    @Test
    void testGetFriendsUsernames() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        List<String> friendsUsernames = webSocketService.getFriendsUsernames("username");

        assertEquals(1, friendsUsernames.size());
        assertEquals("friend@example.com", friendsUsernames.get(0));
    }

    @Test
    void testAcceptFriendRequest() {
        // Arrange
        FriendRequest request = new FriendRequest("sender@example.com", "receiver@example.com", "ACCEPTED");
    
        User sender = new User();
        sender.setUsername("sender@example.com");
        sender.setPassword("password");
        sender.setFirstName("Sender");
        sender.setLastName("User");
        sender.setFriends(new ArrayList<>()); 
        sender.setPendingFriendRequests(new ArrayList<>());
        sender.setHikes(new ArrayList<>());
    
        User receiver = new User();
        receiver.setUsername("receiver@example.com");
        receiver.setPassword("password");
        receiver.setFirstName("Receiver");
        receiver.setLastName("User");
        receiver.setFriends(new ArrayList<>());
        receiver.setPendingFriendRequests(new ArrayList<>());
        receiver.setHikes(new ArrayList<>());
    
        when(userRepository.findByUsername("sender@example.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByUsername("receiver@example.com")).thenReturn(Optional.of(receiver));
    
        // Act
        webSocketService.acceptFriendRequest(request);

        // Assert
        verify(userRepository).save(sender);
        verify(userRepository).save(receiver);
        assertTrue(sender.getFriends().stream().anyMatch(friend -> "receiver@example.com".equals(friend.getUsernameFriend())));
        assertTrue(receiver.getFriends().stream().anyMatch(friend -> "sender@example.com".equals(friend.getUsernameFriend())));
    }
    

    @Test
    void testCheckForUser() {
        // Arrange
        Principal principal = () -> "username";
        User friendUser = new User(ObjectId.get(), "friend@example.com", "password456", "Friend", "User", 
                null, null, null, "active", null);
        User loggedInUser = mock(User.class);

        when(userRepository.findByUsername("friend@example.com")).thenReturn(Optional.of(friendUser));
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(loggedInUser));
        when(loggedInUser.getFriends()).thenReturn(List.of()); 

        // Act
        boolean result = webSocketService.checkForUser(principal, "friend@example.com");

        // Assert
        assertTrue(result);
        verify(loggedInUser).getFriends();
    }

    @Test
    void testRemoveFriendRequest() {
        // Arrange
        FriendRequest request = new FriendRequest("sender@example.com", "receiver@example.com", "PENDING");
        User sender = new User();
        sender.setUsername("sender@example.com");
        sender.setPassword("password");
        sender.setFirstName("Sender");
        sender.setLastName("User");
        sender.setFriends(new ArrayList<>());
        sender.setPendingFriendRequests(new ArrayList<>());
        sender.getPendingFriendRequests().add("receiver@example.com");

        when(userRepository.findByUsername("sender@example.com")).thenReturn(Optional.of(sender));

        // Act
        webSocketService.removeFriendRequest(request);

        // Assert
        verify(userRepository).save(sender);
        assertFalse(sender.getPendingFriendRequests().contains("receiver@example.com"));
    }
}
