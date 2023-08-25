package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.service.CreditCardService;
import com.shepherdmoney.interviewproject.utils.ResponseWrapper;
import com.shepherdmoney.interviewproject.vo.request.AddCreditCardToUserPayload;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
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

    private final CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    /**
     * Endpoint to add a new credit card and associate it with a user.
     */
    @PostMapping("/credit-card")
    public ResponseEntity<ResponseWrapper> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
        CreditCard newCreditCard = new CreditCard();
        newCreditCard.setIssuanceBank(payload.getCardIssuanceBank());
        newCreditCard.setNumber(payload.getCardNumber());

        CreditCard createdCreditCard = creditCardService.addCreditCardToUser(payload.getUserId(), newCreditCard);
        ResponseWrapper response = new ResponseWrapper("Credit card with number: " + createdCreditCard.getNumber()
                + " successfully added to user with id: " + payload.getUserId());
        response.setId(createdCreditCard.getId());

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to retrieve all credit cards owned by a user.
     */
    @GetMapping("/credit-card:all")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {
        List<CreditCardView> creditCardViews = creditCardService.getAllCreditCardsByUserId(userId);
        return ResponseEntity.ok(creditCardViews);
    }

    /**
     * Endpoint to retrieve the user ID associated with a credit card number.
     */
    @GetMapping("/credit-card:user-id")
    public ResponseEntity<Integer> getUserIdForCreditCard(@RequestParam String creditCardNumber) {
        User associatedUser = creditCardService.getUserByCreditCardNumber(creditCardNumber);
        return ResponseEntity.ok(associatedUser.getId());
    }

    /**
     * I changed this to a PatchMapping because only one field needs to be updated.
     * By partially updating instead of replacing the entire record
     * it can positively improve performance. Orphan removal has been set to true
     * so credit cards cannot exist without an associated user
     */
    @PatchMapping("/credit-card:update-balance-history")
    public ResponseEntity<String> updateBalanceHistory(@RequestBody List<UpdateBalancePayload> payload) {
        creditCardService.updateCreditCardBalances(payload);
        return ResponseEntity.ok("Transactions successfully processed");
    }

    /**
     * Endpoint to retrieve credit card details by card number.
     */
    @GetMapping("/credit-card/{cardNumber}")
    public ResponseEntity<CreditCard> getCreditCard(@PathVariable String cardNumber) {
        return ResponseEntity.ok(creditCardService.getCreditCardByNumber(cardNumber));
    }
}