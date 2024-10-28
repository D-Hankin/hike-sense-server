package com.hikesenseserver.hikesenseserver.services;

import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hikesenseserver.hikesenseserver.components.JwtComponent;
import com.hikesenseserver.hikesenseserver.models.Hike;
import com.hikesenseserver.hikesenseserver.models.User;
import com.hikesenseserver.hikesenseserver.repositories.UserRepository;

import jakarta.validation.Valid;

@Service
public class HikeService {

     @Autowired
    JwtComponent jwtCreationComponent;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<String> newHike(Hike hike) {
        hike.setId(UUID.randomUUID().toString());
        hike.setCompleted(false);
        hike.setDateCreated(new Date());
        try {
            System.out.println("Adding hike to user account.");
            UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

            User user = userRepository.findByUsername(userDetails.getUsername())
                                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            user.getHikes().add(hike);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                                .body("New hike saved!");
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(null);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(null);
        } catch (Exception ex) {
            System.out.println("Error adding hike to user account: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(null);
        }
    }

    public ResponseEntity<String> finishHike(@Valid Hike hike) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

            User user = userRepository.findByUsername(userDetails.getUsername())
                                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            for (Hike h : user.getHikes()) {
                if (h.getName().equals(hike.getName())) {
                    h.setStartTime(hike.getStartTime());
                    h.setFinishTime(hike.getFinishTime());
                    h.setDuration(hike.getDuration());
                    h.setAvgHeartRate(hike.getAvgHeartRate());
                    h.setAlerts(hike.getAlerts());
                    h.setFavorite(hike.isFavorite());
                    userRepository.save(user);
                    return ResponseEntity.status(HttpStatus.OK)
                                        .body("Hike completed!");
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("Hike not found.");
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(null);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(null);
        } catch (Exception ex) {
            System.out.println("Error finishing hike: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(null);
        }
    }

}
