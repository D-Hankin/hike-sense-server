package com.hikesenseserver.hikesenseserver.services;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hikesenseserver.hikesenseserver.models.Friend;
import com.hikesenseserver.hikesenseserver.models.FriendRequest;
import com.hikesenseserver.hikesenseserver.models.User;
import com.hikesenseserver.hikesenseserver.repositories.UserRepository;

@Service
public class WebSocketService {
    
    @Autowired
    private UserRepository userRepository;

    public List<String> getFriendsUsernames(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getFriends().stream().map(Friend::getUsernameFriend).collect(Collectors.toList());
    }

    public void acceptFriendRequest(FriendRequest request) {
        User sender = userRepository.findByUsername(request.getSender()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        User receiver = userRepository.findByUsername(request.getReceiver()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        sender.getFriends().add(new Friend(receiver.getUsername(), receiver.getFirstName(), receiver.getLastName(), receiver.getHikes()));
        receiver.getFriends().add(new Friend(sender.getUsername(), sender.getFirstName(), sender.getLastName(), sender.getHikes()));
        sender.getPendingFriendRequests().remove(request.getReceiver());
        userRepository.save(sender);
        userRepository.save(receiver);
    }

    public boolean checkForUser(Principal user, String receiver) {
        
        return userRepository.findByUsername(receiver).isPresent() 
                && userRepository.findByUsername(user.getName()).get().getFriends().stream().noneMatch(friend -> friend.getUsernameFriend().equals(receiver)) 
                && !receiver.equals(user.getName()); 
    }

    public boolean checkForExistingFriendRequest(FriendRequest request) {
        return userRepository.findByUsername(request.getReceiver()).get().getPendingFriendRequests().stream().noneMatch(fr -> fr.equals(request.getSender()));
    }

    public void removeFriendRequest(FriendRequest request) {
        
        User sender = userRepository.findByUsername(request.getSender()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        sender.getPendingFriendRequests().remove(request.getReceiver());
        userRepository.save(sender);
    }
}
