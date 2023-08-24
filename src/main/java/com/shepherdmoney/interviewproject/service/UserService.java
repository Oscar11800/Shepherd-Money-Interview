package com.shepherdmoney.interviewproject.service;

import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    final UserRepository userRepo;

/*
    constructor dependency injection for guaranteeing
    immutability of the state of the bean after creation
*/
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

}
