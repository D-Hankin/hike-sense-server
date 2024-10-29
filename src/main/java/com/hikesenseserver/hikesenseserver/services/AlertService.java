package com.hikesenseserver.hikesenseserver.services;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hikesenseserver.hikesenseserver.config.EmailConfig;
import com.hikesenseserver.hikesenseserver.models.Friend;
import com.hikesenseserver.hikesenseserver.models.HeartRateAlert;
import com.hikesenseserver.hikesenseserver.models.User;
import com.hikesenseserver.hikesenseserver.repositories.UserRepository;

import jakarta.validation.Valid;

@Service
public class AlertService {

    @Autowired
    EmailConfig emailConfig;

    @Autowired
    UserRepository userRepository;
    
    public ResponseEntity<String> sendAlert(@Valid HeartRateAlert alert) {
         UserDetails userDetails = (UserDetails) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println("SOS sent by " + user.getUsername());

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", emailConfig.getEmailHost());
        props.put("mail.smtp.port", emailConfig.getEmailPort());

        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailConfig.getEmailUsername(), emailConfig.getEmailPassword());
                }
            });

        for (Friend friend : user.getFriends()) {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(emailConfig.getEmailUsername()));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(friend.getUsernameFriend()));
                message.setSubject("SOS Message: " + user.getUsername());
                message.setText("Alert " + friend.getFirstNameFriend() + "!\n\n" + user.getUsername() + " is out hiking and has unusual heartrate activity: \n\n" + alert.getMessage() + "\n\nLocation - Latitude" + alert.getLocation().getLatitude() + ", Longitude: " + alert.getLocation().getLongitude() + "\nTime - " + alert.getTime());
    
                Transport.send(message);
    
                System.out.println("Email sent successfully to " + friend.getUsernameFriend());
    
            } catch (MessagingException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body("Email not sent");
            }
            
        }

        return ResponseEntity.ok("Alert sent");
    }
}