package com.shepherdmoney.interviewproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "MyUser")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CreditCard> creditCards = new ArrayList<>();

    //set up bidirectional relationship with CreditCard class
    public void addCreditCard(CreditCard creditCard) {
        //adds card to user's credit cards
        creditCards.add(creditCard);
        //sets user as the owner of this card
        creditCard.setUser(this);
    }

    // TODO: User's credit card DONE
    // HINT: A user can have one or more, or none at all. We want to be able to query credit cards by user
    //       and user by a credit card.
}
