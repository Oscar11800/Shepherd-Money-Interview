package com.shepherdmoney.interviewproject.service;

import com.shepherdmoney.interviewproject.exception.CreditCardNotFoundException;
import com.shepherdmoney.interviewproject.exception.UserNotFoundException;
import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public List<CreditCardView> getAllCreditCardsByUserId(int userId) {
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

    public CreditCard getCreditCardByNumber(String creditCardNumber) {
        try {
            return creditCardRepo.findCreditCardByNumber(creditCardNumber);
        } catch (Exception ex) {
            throw new CreditCardNotFoundException("Credit card with number " + creditCardNumber + " not found.");
        }
    }

    public User getUserByCreditCardNumber(String creditCardNumber) {
        CreditCard creditCard = getCreditCardByNumber(creditCardNumber);
        Optional<User> userOptional = Optional.ofNullable(creditCard).map(CreditCard::getUser);

        return userOptional.orElseThrow(() -> new UserNotFoundException("No user associated with credit card number " + creditCardNumber));
    }

    public CreditCard addCreditCardToUser(int userId, CreditCard creditCard) {
        User user = userService.getUser(userId);
        creditCard.setUser(user);
        return creditCardRepo.save(creditCard);
    }

    /*
     * Partial credit card update for when we don't want to
     * replace all fields. This can improve performance
     * when updating entire entity is unnecessary
     */
    public void updateCreditCardBalances(List<UpdateBalancePayload> balanceUpdates) {
        for (UpdateBalancePayload updatePayload : balanceUpdates) {
            String creditCardNumber = updatePayload.getCreditCardNumber();
            CreditCard creditCard = creditCardRepo.findCreditCardByNumber(creditCardNumber);

            if (creditCard != null) {
                BalanceHistory balanceHistory = new BalanceHistory();
                balanceHistory.setDate(updatePayload.getTransactionTime());
                balanceHistory.setBalance(updatePayload.getTransactionAmount());
                creditCard.getBalanceHistory().add(balanceHistory);
                creditCardRepo.save(creditCard);
            } else {
                throw new CreditCardNotFoundException(
                        "Credit Card with number{" + creditCardNumber + "} not found.");
            }
        }
    }
}
