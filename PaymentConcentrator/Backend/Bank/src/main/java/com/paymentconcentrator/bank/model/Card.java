package com.paymentconcentrator.bank.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String number;

    @Column
    private String securityCode;

    @Column
    private String expDate;

    @Column
    private String cardHolderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account", referencedColumnName = "id")
    private Account account;
}
