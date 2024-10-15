package com.hikesenseserver.hikesenseserver.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hikesenseserver.hikesenseserver.models.UserDto;
import com.hikesenseserver.hikesenseserver.repositories.UserRepository;

@Service
public class JpaUserDetailService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    
    public JpaUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        return userRepository.findByUsername(username)
            .map(UserDto::new)
            .orElseThrow(() -> new UsernameNotFoundException("No user found"));
    }
}
