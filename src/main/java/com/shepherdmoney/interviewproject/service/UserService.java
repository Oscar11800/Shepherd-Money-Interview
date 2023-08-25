package com.shepherdmoney.interviewproject.service;

import com.shepherdmoney.interviewproject.exception.CreditCardNotFoundException;
import com.shepherdmoney.interviewproject.exception.UserNotFoundException;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public User getUser(int id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isEmpty()) {
            throw new CreditCardNotFoundException("Credit Card with id{" + id + "} not found.");
        }
        return user.get();
    }

    public User assignCreditCard(int id, CreditCard creditCard){
        User user = getUser(id);
        user.addCreditCard(creditCard);
        return userRepo.save(user);
    }

    public User createUser(User user){
        return userRepo.save(user);
    }

    public void deleteUser(int id){
        Optional<User> user = userRepo.findById(id);
        if(user.isEmpty()){
            throw new UserNotFoundException("User with id{ " + id + " } is not found.");
        }
        userRepo.delete(user.get());
    }

}
