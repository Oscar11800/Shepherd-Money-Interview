package com.shepherdmoney.interviewproject.service;

import com.shepherdmoney.interviewproject.exceptions.CreditCardNotFoundException;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public CreditCard getCreditCard(int id) {
        Optional<CreditCard> tempCreditCard = creditCardRepo.findById(id);
        if (tempCreditCard.isEmpty()) {
            throw new CreditCardNotFoundException("Credit Card with id{" + id + "} not found.");
        }

        return tempCreditCard.get();
    }

    public List<CreditCard> getAllCreditCards() {
        return creditCardRepo.findAll();
    }

    public CreditCard addCreditCard(CreditCard creditCard) {
        return creditCardRepo.save(creditCard);
    }

    public CreditCard updateCreditCard(int id, CreditCard creditCard) {
        Optional<CreditCard> tempCreditCard = creditCardRepo.findById(id);
        if (tempCreditCard.isEmpty()) {
            throw new CreditCardNotFoundException("Credit Card with id{" + id + "} not found.");
        }
        creditCard.setId(id);
        return creditCardRepo.save(creditCard);
    }

    /*
    * Partial record update for when we don't want to
    * replace all fields. This can improve performance
    * when updating entire entity is unnecessary
    * @param id identifier of credit card to modify
    * @param Map<String, Object> key, value pairs fed to HTTP request body to modify
     */

    public CreditCard patch(int id, Map<String, Object> partialCreditCard) {
        Optional<CreditCard> creditCard = creditCardRepo.findById(id);

        /*
        loop through Map of key-value pairs to be updated, makes desired changes
        Using the Reflections API to modify CreditCard object during runtime
        */
        if (creditCard.isPresent()) {
            partialCreditCard.forEach((key, value) -> {
                System.out.println("Key: " + key + " Value: " + value);
                Field field = ReflectionUtils.findField(CreditCard.class, key);
                //allows access to private fields
                ReflectionUtils.makeAccessible(field);
                //sets the field in the CreditCard object
                ReflectionUtils.setField(field, creditCard.get(), value);
            });
        } else {
            throw new CreditCardNotFoundException("Credit Card with id{\" + id + \"} not found.");
        }
        return creditCardRepo.save(creditCard.get());
    }

}
