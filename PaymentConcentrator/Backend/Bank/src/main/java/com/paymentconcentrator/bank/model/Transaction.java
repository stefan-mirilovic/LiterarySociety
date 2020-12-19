package com.paymentconcentrator.bank.model;

import com.paymentconcentrator.bank.enumeration.TransactionStatus;
import com.paymentconcentrator.bank.enumeration.TransactionType;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transaction")
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

    @Column
    private Long paymentId;

    @Column
    private TransactionStatus status;

    @Column
    private Long merchantOrderId;

    @Column
    private String successUrl;

    @Column
    private String failedUrl;

    @Column
    private String errorUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account", referencedColumnName = "id")
    private Account account;

    public Transaction() {
    }

    public Transaction(Double amount, LocalDateTime timestamp, TransactionType type, Long paymentId, TransactionStatus status, Account account, String successUrl, String failedUrl, String errorUrl, Long merchantOrderId) {
        this.amount = amount;
        this.timestamp = timestamp;
        this.type = type;
        this.paymentId = paymentId;
        this.status = status;
        this.successUrl = successUrl;
        this.failedUrl = failedUrl;
        this.errorUrl = errorUrl;
        this.account = account;
        this.merchantOrderId = merchantOrderId;
    }
}
