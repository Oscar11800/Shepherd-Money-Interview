package com.shepherdmoney.interviewproject.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MyUser")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CreditCard> creditCards = new ArrayList<>();

    // Set up bidirectional relationship with CreditCard class
    // A user can have one or more, or none at all.
    // We want to be able to query credit cards by user
    // and user by a credit card.
    public void addCreditCard(CreditCard creditCard) {
        // Adds card to user's credit cards
        creditCards.add(creditCard);
        // Sets user as the owner of this card
        creditCard.setUser(this);
    }
}