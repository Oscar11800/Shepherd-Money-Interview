package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.exceptions.CreditCardNotFoundException;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.service.UserService;
import com.shepherdmoney.interviewproject.vo.request.CreateUserPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    // TODO: wire in the user repository (~ 1 line)
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/user")
    public ResponseEntity<Integer> createUser(@RequestBody CreateUserPayload payload) {
        // TODO: Create an user entity with information given in the payload, store it in the database
        //       and return the id of the user in 200 OK response
        User newUser = new User();
        newUser.setName(payload.getName());
        newUser.setEmail(payload.getEmail());
        User createdUser = userService.createUser(newUser);

        return ResponseEntity.ok(createdUser.getId());
    }
    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestParam int userId) {
        // TODO: Return 200 OK if a user with the given ID exists, and the deletion is successful
        //       Return 400 Bad Request if a user with the ID does not exist
        //       The response body could be anything you consider appropriate
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("User with ID " + userId + " has been deleted.");
        } catch (CreditCardNotFoundException ex) {
            return ResponseEntity.badRequest().body("User with ID " + userId + " not found.");
        }
    }
}
