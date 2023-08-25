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

/*
 * I decided to return Response entities instead of POJOs
 * to maintain more control over HTTP requests in case we want to
 * provide additional metadata in the future.
 */
@RestController
public class UserController {

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
        //service layer tries and catches exception
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        userService.getUser(id);
        return ResponseEntity.ok().build();
    }
}
