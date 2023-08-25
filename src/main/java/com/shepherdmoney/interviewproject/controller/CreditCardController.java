package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.service.CreditCardService;
import com.shepherdmoney.interviewproject.utils.ResponseWrapper;
import com.shepherdmoney.interviewproject.vo.request.AddCreditCardToUserPayload;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import org.springframework.http.HttpStatus;
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

    /*
     * Given a credit card payload, create a new credit card and assign it to a user
     */
    @PostMapping("/credit-card")
    public ResponseEntity<ResponseWrapper> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
        //create credit card entity
        CreditCard newCreditCard = new CreditCard();
        newCreditCard.setIssuanceBank(payload.getCardIssuanceBank());
        newCreditCard.setNumber(payload.getCardNumber());

        //associate card with user
        CreditCard createdCreditCard = creditCardService.addCreditCardToUser(payload.getUserId(), newCreditCard);
        ResponseWrapper response = new ResponseWrapper();

        response.setId(createdCreditCard.getId());

        return ResponseEntity.ok(response);
    }

    /*
    * Given a userId, returns all credit cards the user owns
    */
    @GetMapping("/credit-card:all")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {
        List<CreditCardView> creditCardViews = creditCardService.getAllCreditCardsByUserId(userId);
        return ResponseEntity.ok(creditCardViews);
    }

    @GetMapping("/credit-card:user-id")
    public ResponseEntity<Integer> getUserIdForCreditCard(@RequestParam String creditCardNumber) {
        // TODO: Given a credit card number, efficiently find whether there is a user associated with the credit card DONE
        //       If so, return the user id in a 200 OK response. If no such user exists, return 400 Bad Request

        User associatedUser = creditCardService.getUserByCreditCardNumber(creditCardNumber);
        return ResponseEntity.ok(associatedUser.getId());
    }

    /*
     * I changed this to a PatchMapping because only one field needs to be updated.
     * By partially updating instead of replacing the entire record
     * it can positively improve performance. Orphan removal has been set to true
     * so credit cards cannot exist without an associated user
     */
    @PatchMapping("/credit-card:update-balance")
    public ResponseEntity<Integer> postMethodName(@RequestBody List<UpdateBalancePayload> payload) {
        //TODO: Given a list of transactions, update credit cards' balance history.
        //      For example: if today is 4/12, a credit card's balanceHistory is [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}],
        //      Given a transaction of {date: 4/10, amount: 10}, the new balanceHistory is
        //      [{date: 4/12, balance: 120}, {date: 4/11, balance: 110}, {date: 4/10, balance: 110}]
        //      Return 200 OK if update is done and successful, 400 Bad Request if the given card number
        //        is not associated with a card.

        creditCardService.updateCreditCardBalances(payload);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/credit-card/{cardNumber}")
    public ResponseEntity<CreditCard> getCreditCard(@PathVariable String cardNumber){
        return new ResponseEntity<>(creditCardService.getCreditCardByNumber(cardNumber), HttpStatus.OK);
    }
}
