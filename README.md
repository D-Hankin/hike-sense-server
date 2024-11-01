HikeSense Server
================

HikeSense is a platform designed to enhance hiking safety and social experience by providing real-time data monitoring, alerting functionalities, friend connections, and chat features. This server component of the HikeSense project powers backend operations, including user authentication, subscription management, and WebSocket-based real-time communication for location and safety monitoring.

* * * * *

Stack
--------

1. **Client**: /hike-sense-client
2. **App**: /hike-sense-app
3. **Arduino**: /hike-sense-arduino

Features
--------

1.  **User Management**:

    -   User sign-up, login, and JWT-based authentication.
    -   Account management with friend connections for safety tracking.
2.  **Hiking Data Collection**:

    -   Storage and retrieval of hike details, including route, heart rate, temperature, and more.
3.  **Safety Alerts**:

    -   Alerts based on heart rate thresholds, customized for each user.
    -   Location markers sent as email notications to users contacts.
4.  **Subscription Management**:

    -   Integration with Stripe for upgrading subscription plans.
5.  **Real-Time Communication**:

    -   WebSocket-based chat for friend-to-friend messaging.
    -   Real-time updates on user location and safety stats.

Technologies
------------

-   **Backend Framework**: Spring Boot
-   **Database**: MongoDB (hosted on MongoDB Atlas)
-   **Containerization**: Docker
-   **Payment**: Stripe API
-   **AI API**: Open Ai
-   **Real-Time Communication**: WebSocket


