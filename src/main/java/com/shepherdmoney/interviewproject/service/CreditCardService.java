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

    public CreditCardService(CreditCardRepository creditCardRepo,
                             UserService userService) {
        this.creditCardRepo = creditCardRepo;
        this.userService = userService;
    }

    // Retrieves a list of CreditCardView objects associated with a user.
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

    // Retrieves a CreditCard by its number.
    public CreditCard getCreditCardByNumber(String creditCardNumber) {
        CreditCard creditCard = creditCardRepo.findCreditCardByNumber(creditCardNumber);
        if (creditCard == null) {
            throw new CreditCardNotFoundException("Credit card with number " + creditCardNumber + " not found.");
        }
        return creditCard;
    }

    // Retrieves a User associated with a credit card number.
    public User getUserByCreditCardNumber(String creditCardNumber) {
        CreditCard creditCard = getCreditCardByNumber(creditCardNumber);
        Optional<User> userOptional = Optional.ofNullable(creditCard).map(CreditCard::getUser);

        return userOptional.orElseThrow(() -> new UserNotFoundException("No user associated with credit card number " + creditCardNumber));
    }

    // Adds a new credit card to a user.
    public CreditCard addCreditCardToUser(int userId, CreditCard creditCard) {
        // Exception checking done in userService
        User user = userService.getUser(userId);
        creditCard.setUser(user);
        return creditCardRepo.save(creditCard);
    }

    /*
     * Partial credit card update for when we don't want to
     * replace all fields. This can improve performance
     * when updating the entire entity is unnecessary.
     * Assumptions: transactions are not retroactive
     * (not taking effect from the past), there can be
     * multiple transactions on a single date, and therefore
     * multiple balance histories can share a date.
     *
     * Takes in a list of transactions and updates the
     * respective credit cards' balances.
     */
    public void updateCreditCardBalances(List<UpdateBalancePayload> transactions) {
        for (UpdateBalancePayload transaction : transactions) {
            // Identify which credit card to transact from.
            String creditCardNumber = transaction.getCreditCardNumber();
            CreditCard creditCard = creditCardRepo.findCreditCardByNumber(creditCardNumber);

            // If unused card, no previous balance; else, use the latest balance.
            if (creditCard != null) {
                BalanceHistory balanceHistory = getBalanceHistory(transaction, creditCard);

                // Update balance history with the new transaction.
                creditCard.addBalanceHistoryEntry(balanceHistory);
                creditCardRepo.save(creditCard);
                System.out.println(creditCard.getBalanceHistoryList());
            } else {
                throw new CreditCardNotFoundException("Credit Card with number " + creditCardNumber + " not found.");
            }
        }
    }

    // Helper method to obtain latest balance for a new transaction
    private BalanceHistory getBalanceHistory(UpdateBalancePayload transaction, CreditCard creditCard) {
        // Check if the card has any previous balance history (using ordered balance history)
        boolean unusedCard = creditCard.getBalanceHistoryList().isEmpty();
        double currentBalance = unusedCard ? 0 : creditCard.getSortedBalanceHistoryByDate().get(0).getBalance();
        System.out.println(currentBalance);

        // Add transaction as a new balance history.
        BalanceHistory balanceHistory = new BalanceHistory();
        balanceHistory.setDate(transaction.getTransactionTime());
        // Calculate the new balance after the transaction.
        double newBalance = currentBalance + transaction.getTransactionAmount();
        balanceHistory.setBalance(newBalance);
        return balanceHistory;
    }
}