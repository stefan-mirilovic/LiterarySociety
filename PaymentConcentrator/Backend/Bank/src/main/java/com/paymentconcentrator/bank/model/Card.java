package com.paymentconcentrator.bank.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String number;

    @Column
    private String securityCode;

    @Column
    private String expDate;

    @Column
    private String cardHolderName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account", referencedColumnName = "id")
    private Account account;
}
