package com.hikesenseserver.hikesenseserver;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hikesenseserver.hikesenseserver.models.CustomerAddress;
import com.hikesenseserver.hikesenseserver.models.User;
import com.hikesenseserver.hikesenseserver.repositories.UserRepository;
import com.hikesenseserver.hikesenseserver.services.StripeService;
import com.stripe.exception.StripeException;

import java.util.Map;
import java.util.Optional;

// ****** THESE TESTS UNFOTUNATELY DO NOT PASS: STRIPE REFUSES TO TREAT AS TESTS AND CONTINUES TO ATTEMPT REAL COMMUNICATION WITH THE STRIPE API. NO SOLUTION FOUND ******

public class StripeServiceTest {
    
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StripeService stripeService;

    private User user;
    private static final String TEST_API_KEY = "sk_test_mockedKey";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("testUser");
        user.setSubscriptionStatus("free");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    }

    @Test
    public void upgradeSubscriptionShouldReturnSubscriptionData() throws StripeException {
        Map<String, String> response = stripeService.upgradeSubscription(TEST_API_KEY, new CustomerAddress());

        assertNotNull(response.get("subscriptionId"));
        verify(userRepository).save(user);
    }

    @Test
    public void cancelSubscriptionShouldReturnSuccessMessage() throws StripeException {
        Map<String, String> response = stripeService.cancelSubscription("sub_testSubscriptionId");
        assertEquals("Subscription cancelled successfully.", response.get("status"));
        verify(userRepository).save(user);
    }
}
