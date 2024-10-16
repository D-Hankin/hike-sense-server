package com.hikesenseserver.hikesenseserver.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.hikesenseserver.hikesenseserver.models.User;

import org.bson.types.ObjectId;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByUsername(String username); 
    UserDetails findUserDetailsByUsername(String username);
}
