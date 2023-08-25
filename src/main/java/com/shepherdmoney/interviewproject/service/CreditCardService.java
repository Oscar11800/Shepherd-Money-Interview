package com.shepherdmoney.interviewproject.service;

import com.shepherdmoney.interviewproject.exception.CreditCardNotFoundException;
import com.shepherdmoney.interviewproject.exception.UserNotFoundException;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
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

    private final CreditCardRepository creditCardRepo;
    private final UserService userService;

    public CreditCardService(CreditCardRepository creditCardRepo, UserService userService) {
        this.creditCardRepo = creditCardRepo;
        this.userService = userService;
    }

    public CreditCard getCreditCard(int id) {
        Optional<CreditCard> creditCard = creditCardRepo.findById(id);
        if (creditCard.isEmpty()) {
            throw new CreditCardNotFoundException("Credit Card with id{" + id + "} not found.");
        }

        return creditCard.get();
    }

    public List<CreditCard> getAllCreditCards() {
        return creditCardRepo.findAll();
    }

    public List<CreditCardView> getAllCreditCardsByUserId(int userId){
        User user = userService.getUser(userId);
        List<CreditCard> creditCards = user.getCreditCards();

        List<CreditCardView> creditCardViews = new ArrayList<>();
        for (CreditCard creditCard : creditCards) {
            CreditCardView creditCardView = new CreditCardView();
            creditCardView.setIssuanceBank(creditCard.getIssuanceBank());
            creditCardView.setNumber(creditCard.getNumber());
            creditCardViews.add(creditCardView);
        }

        return creditCardViews;
    }

    public CreditCard getCreditCardByNumber(String creditCardNumber){
        try {
            return creditCardRepo.findCreditCardByNumber(creditCardNumber);
        } catch (Exception ex) {
            throw new CreditCardNotFoundException("Credit card with number " + creditCardNumber + " not found.");
        }
    }

    public User getUserByCreditCardNumber(String creditCardNumber){
        CreditCard creditCard = getCreditCardByNumber(creditCardNumber);
        Optional<User> userOptional = Optional.ofNullable(creditCard).map(CreditCard::getUser);

        return userOptional.orElseThrow(() -> new UserNotFoundException("No user associated with credit card number " + creditCardNumber));
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

    public CreditCard addCreditCardToUser(int userId, CreditCard creditCard) {
        User user = userService.getUser(userId);
        creditCard.setUser(user);
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
