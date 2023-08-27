# Project Overview

This project is built with Java, Spring Boot, RESTful API design, database interactions, exception handling, and software architecture. The application provides functionalities to manage credit card information, users, and their associated transactions. Below is a breakdown of the key components and features of the project.

## Components

### 1. CreditCardController

This controller handles various endpoints related to credit card management. It allows users to add credit cards, retrieve all cards associated with a user, retrieve the user ID for a credit card number, update balance history, and retrieve credit card details by card number.

### 2. UserController

The UserController manages user-related endpoints. It enables the creation of new users, deletion of users, and retrieval of user details by their ID.

### 3. Exception Handling

The custom exception classes, such as CreditCardNotFoundException and UserNotFoundException, ensure proper error handling and response generation. The ExceptionHandler class centralizes exception handling and converts them into consistent error responses.

### 4. Repositories

The repositories, CreditCardRepository and UserRepository, manage data persistence and interactions with the database.

### 5. Services

The CreditCardService and UserService classes handle business logic, separating it from the controllers. They manage user and credit card operations, user-card relationships, transaction updates, and more.

### 6. Payloads

The project includes various payload classes, such as AddCreditCardToUserPayload and UpdateBalancePayload, used to structure incoming request data.

### 7. Utilities

The ResponseWrapper class encapsulates API responses, providing a unified format for conveying operation outcomes.

## Features

- Adding credit cards to users, mimicking the one-to-many relationship between them.
- Retrieving all credit cards owned by a user.
- Associating a user with a credit card number and vice versa.
- Partially updating credit card balance history, enhancing performance.
- Retrieving credit card details by card
