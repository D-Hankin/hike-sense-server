package com.hikesenseserver.hikesenseserver.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.hikesenseserver.hikesenseserver.models.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username); 
    UserDetails findUserDetailsByUsername(String username);
}
