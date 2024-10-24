package com.hikesenseserver.hikesenseserver.services;

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

	public void sendFriendRequest(FriendRequest request) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'sendFriendRequest'");
	}

    public void acceptFriendRequest(FriendRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'acceptFriendRequest'");
    }

    public void declineFriendRequest(FriendRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'declineFriendRequest'");
    }
}
