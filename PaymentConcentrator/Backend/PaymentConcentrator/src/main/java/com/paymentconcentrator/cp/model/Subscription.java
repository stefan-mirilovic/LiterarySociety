package com.paymentconcentrator.cp.model;

import com.paymentconcentrator.cp.enumeration.SubscriptionStatus;
import com.paymentconcentrator.cp.enumeration.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private Long id;

    @Column
    private LocalDateTime timestamp;

    @Column
    private Double amount;

    @Column
    private SubscriptionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant", referencedColumnName = "id")
    private Merchant merchant;

    @Column
    private String frequency;

    @Column
    private String interval;

    @Column
    private int cycles;
}
