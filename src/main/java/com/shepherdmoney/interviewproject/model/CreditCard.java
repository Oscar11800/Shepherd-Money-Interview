package com.shepherdmoney.interviewproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String issuanceBank;
    private String number;

    // Establish a many-to-one relationship with the User class
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User user;

    // Set up a one-to-many relationship with BalanceHistory class
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "creditcard_id")
    @JsonIgnore
    private List<BalanceHistory> balanceHistoryList = new ArrayList<>();

    /**
     * Sort the balance history by date in descending order.
     * This ensures that the most recent transactions appear first.
     */
    public List<BalanceHistory> getSortedBalanceHistoryByDate() {
        List<BalanceHistory> sortedList = new ArrayList<>(balanceHistoryList);
        if (!sortedList.isEmpty()) {
            sortedList.sort(Comparator.comparing(BalanceHistory::getDate).reversed());
        }
        return sortedList;
    }

    /**
     * Add a new balance entry to the balance history list and then sort it.
     *
     * @param entry The balance history entry to add.
     */
    public void addBalanceHistoryEntry(BalanceHistory entry) {
        balanceHistoryList.add(entry);
    }
}