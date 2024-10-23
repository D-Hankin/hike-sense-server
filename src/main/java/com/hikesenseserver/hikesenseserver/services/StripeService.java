package com.hikesenseserver.hikesenseserver.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hikesenseserver.hikesenseserver.models.CustomerAddress;
import com.hikesenseserver.hikesenseserver.models.User;
import com.hikesenseserver.hikesenseserver.repositories.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.param.CustomerCreateParams.Address;
import com.stripe.param.SubscriptionCreateParams.PaymentSettings.SaveDefaultPaymentMethod;
import com.stripe.model.Customer;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SubscriptionCreateParams;

@Service
public class StripeService {

    @Autowired
    UserRepository userRepository;

    public Map<String, String> upgradeSubscription(String productId, CustomerAddress address) throws StripeException {
        System.out.println("Upgrading subscription...");

        Product product = Product.retrieve(productId);
        System.out.println("Product retrieved: " + product.getName());

        UserDetails userDetails = (UserDetails) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Price price = Price.retrieve(product.getDefaultPrice());
        System.out.println("Price retrieved: " + price.getUnitAmount());
        
        CustomerCreateParams customerParams = CustomerCreateParams.builder()
            .setName(user.getFirstName() + " " + user.getLastName())
            .setEmail(user.getUsername())
            .setAddress(Address.builder()
                .setCity(address.getCity())
                .setLine1(address.getAddress1())
                .setLine2(address.getAddress2())
                .setPostalCode(address.getPostnumber())
                .build())
            .build();
        Customer customerFinal = null;
        try {
            customerFinal = Customer.create(customerParams);
        } catch (StripeException e) {
            e.printStackTrace();
        }

        if (customerFinal != null) {
            try {
                SubscriptionCreateParams.PaymentSettings paymentSettings = 
                    SubscriptionCreateParams.PaymentSettings.builder()
                    .setSaveDefaultPaymentMethod(SaveDefaultPaymentMethod.ON_SUBSCRIPTION)
                    .build();

                SubscriptionCreateParams subCreateParams = 
                    SubscriptionCreateParams.builder()
                    .setCustomer(customerFinal.getId())
                    .addItem(SubscriptionCreateParams.Item.builder()
                        .setPrice(price.getId())
                        .build())
                    .setPaymentSettings(paymentSettings)
                    .setPaymentBehavior(SubscriptionCreateParams.PaymentBehavior.DEFAULT_INCOMPLETE)
                    .addAllExpand(Arrays.asList("latest_invoice.payment_intent"))
                    .build();

                Subscription subscription = Subscription.create(subCreateParams);

                HashMap<String, String> responseData = new HashMap<>();
                responseData.put("subscriptionId", subscription.getId());
                responseData.put("clientSecret", subscription.getLatestInvoiceObject().getPaymentIntentObject().getClientSecret());
                
                user.setSubscriptionStatus("premium" + subscription.getId());
                userRepository.save(user);

                return responseData; // Return the response map
            } catch (StripeException e) {
                e.printStackTrace();
                throw e; // Or handle the exception accordingly
            }
        } else {
            System.err.println("Failed to create customer.");
            throw new RuntimeException("Failed to create customer.");
        }
    }
}
