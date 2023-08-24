package com.shepherdmoney.interviewproject.service;

import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
    Credit Card Service class for implementing business logic
    used by the controller and interacting with CreditCardRepository.
    This separates business logic from the controller and is easily testable.
 */
@Service
public class CreditCardService {

    final CreditCardRepository creditCardRepo;

    public CreditCardService(CreditCardRepository creditCardRepo) {
        this.creditCardRepo = creditCardRepo;
    }

    public CreditCard getCreditCard(int id){
        return creditCardRepo.findById(id).get();
    }


}
