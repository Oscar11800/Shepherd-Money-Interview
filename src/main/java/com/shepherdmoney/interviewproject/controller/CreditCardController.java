package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.exception.UserNotFoundException;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.service.CreditCardService;
import com.shepherdmoney.interviewproject.utils.ResponseWrapper;
import com.shepherdmoney.interviewproject.vo.request.AddCreditCardToUserPayload;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/*
 * Assumptions about the relationship between credit card and user:
 *  - They have a one-to-many relationship because one user
 * can have more than one credit card, but a credit card cannot have more
 * than one user (there may be joint/authorized users, but that is not
 * being considered in this application).
 *
 * - They have a bidirectional relationship because we should be able
 * to access credit cards given a user id, and a user given
 * a credit card number.
 *
 * - Every credit card must be associated with a user, and when we
 * delete a user, the associated credit cards should be deleted but
 * not vice versa
 * */
@RestController
public class CreditCardController {
    final CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @PostMapping("/credit-card")
    public ResponseEntity<ResponseWrapper> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
        // TODO: Create a credit card entity, and then associate that credit card with user with given userId
        //       Return 200 OK with the credit card id if the user exists and credit card is successfully associated with the user
        //       Return other appropriate response code for other exception cases
        //       Do not worry about validating the card number, assume card number could be any arbitrary format and length
        try {
            //create credit card entity
            CreditCard newCreditCard = new CreditCard();
            newCreditCard.setIssuanceBank(payload.getCardIssuanceBank());
            newCreditCard.setNumber(payload.getCardNumber());

            //associate card with user
            CreditCard createdCreditCard = creditCardService.addCreditCardToUser(payload.getUserId(), newCreditCard);
            ResponseWrapper response = new ResponseWrapper();

            response.setId(createdCreditCard.getId());

            return ResponseEntity.ok(response);
        } catch (UserNotFoundException ex) {
            ResponseWrapper response = new ResponseWrapper();
            response.setMessage("User with ID " + payload.getUserId() + " not found.");

            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/credit-card:all")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {
        // TODO: return a list of all credit card associated with the given userId, using CreditCardView class
        //       if the user has no credit card, return empty list, never return null
        return null;
    }

    @GetMapping("/credit-card:user-id")
    public ResponseEntity<Integer> getUserIdForCreditCard(@RequestParam String creditCardNumber) {
        // TODO: Given a credit card number, efficiently find whether there is a user associated with the credit card
        //       If so, return the user id in a 200 OK response. If no such user exists, return 400 Bad Request
        return null;
    }

    @PostMapping("/credit-card:update-balance")
    public ResponseEntity<Integer> postMethodName(@RequestBody UpdateBalancePayload[] payload) {
        //TODO: Given a list of transactions, update credit cards' balance history.
        //      For example: if today is 4/12, a credit card's balanceHistory is [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}],
        //      Given a transaction of {date: 4/10, amount: 10}, the new balanceHistory is
        //      [{date: 4/12, balance: 120}, {date: 4/11, balance: 110}, {date: 4/10, balance: 110}]
        //      Return 200 OK if update is done and successful, 400 Bad Request if the given card number
        //        is not associated with a card.

        return null;
    }

}
