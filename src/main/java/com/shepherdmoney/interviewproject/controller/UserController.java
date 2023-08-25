package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.exception.UserNotFoundException;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.service.UserService;
import com.shepherdmoney.interviewproject.vo.request.CreateUserPayload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {

    // TODO: wire in the user repository (~ 1 line)
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<Integer> createUser(@RequestBody CreateUserPayload payload) {
        User newUser = new User();
        newUser.setName(payload.getName());
        newUser.setEmail(payload.getEmail());
        User createdUser = userService.createUser(newUser);

        return ResponseEntity.ok(createdUser.getId());
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User with id {" + id + " } not found.",
                    ex);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        try {
            return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
