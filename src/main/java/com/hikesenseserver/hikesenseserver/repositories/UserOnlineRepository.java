package com.hikesenseserver.hikesenseserver.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import com.hikesenseserver.hikesenseserver.models.UserOnline;

public interface UserOnlineRepository extends MongoRepository<UserOnline, String> {
    @NonNull
    List<UserOnline> findAll();
    UserOnline findByUsername(String username);
    void deleteByUsername(String username);
    
}
