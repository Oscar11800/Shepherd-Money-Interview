package com.shepherdmoney.interviewproject.service;

import com.shepherdmoney.interviewproject.exception.CreditCardNotFoundException;
import com.shepherdmoney.interviewproject.exception.UserNotFoundException;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepo;

    /*
     * Constructor dependency injection for guaranteeing
     * immutability of the state of the bean after creation.
     */
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Retrieve a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The retrieved user.
     * @throws CreditCardNotFoundException If the user is not found.
     */
    public User getUser(int id) {
        Optional<User> userOptional = userRepo.findById(id);
        return userOptional.orElseThrow(
                () -> new CreditCardNotFoundException("User with id{" + id + "} not found.")
        );
    }

    /**
     * Create a new user.
     *
     * @param user The user to be created.
     * @return The created user.
     */
    public User createUser(User user) {
        return userRepo.save(user);
    }

    /**
     * Delete a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @throws UserNotFoundException If the user is not found.
     */
    public void deleteUser(int id) {
        User user = getUser(id);
        userRepo.delete(user);
    }
}