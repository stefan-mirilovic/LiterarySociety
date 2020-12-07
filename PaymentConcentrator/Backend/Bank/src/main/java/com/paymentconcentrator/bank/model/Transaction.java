package com.paymentconcentrator.bank.model;

import com.paymentconcentrator.bank.enumeration.TransactionType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private Double amount;

    @Column
    private LocalDateTime timestamp;

    @Column
    private TransactionType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account", referencedColumnName = "id")
    private Account account;
}
