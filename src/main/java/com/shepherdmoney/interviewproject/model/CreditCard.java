package com.shepherdmoney.interviewproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String issuanceBank;

    private String number;

    //    exclude REMOVE cascade because if card is deleted, associated
    //    user should not be deleted
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="creditcard_id")
    private List<BalanceHistory> balanceHistory = new ArrayList<>();

    public void sortBalanceHistoryByDate(){
        Collections.sort(balanceHistory, Comparator.comparing(BalanceHistory::getDate).reversed());
    }
    public void addBalanceHistoryEntry(BalanceHistory entry){
        balanceHistory.add(entry);
        sortBalanceHistoryByDate();
    }

    // TODO: Credit card's owner. For detailed hint, please see User class DONE

    // TODO: Credit card's balance history. It is a requirement that the dates in the balanceHistory 
    //       list must be in chronological order, with the most recent date appearing first in the list. 
    //       Additionally, the first object in the list must have a date value that matches today's date, 
    //       since it represents the current balance of the credit card. For example:
    //       [
    //         {date: '2023-04-13', balance: 1500},
    //         {date: '2023-04-12', balance: 1200},
    //         {date: '2023-04-11', balance: 1000},
    //         {date: '2023-04-10', balance: 800}
    //       ]
}
