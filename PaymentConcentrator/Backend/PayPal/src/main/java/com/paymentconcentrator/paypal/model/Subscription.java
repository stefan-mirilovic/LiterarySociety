package com.paymentconcentrator.paypal.model;

import com.paymentconcentrator.paypal.enumeration.SubscriptionStatus;
import com.paypal.base.rest.APIContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String agreementToken;

    @Column
    private Double amount;

    @Column
    private LocalDateTime timestamp;

    @Column
    private Long merchantOrderId;

    @Column
    private String successUrl;

    @Column
    private String failedUrl;

    @Column
    private String errorUrl;

    @Column
    private String frequency;

    @Column
    private String interval;

    @Column
    private Integer cycles;

    @ManyToOne
    private Account seller;

    @Column
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;
}
