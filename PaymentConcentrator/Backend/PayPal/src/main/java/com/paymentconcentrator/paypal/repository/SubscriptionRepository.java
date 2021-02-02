package com.paymentconcentrator.paypal.repository;

import com.paymentconcentrator.paypal.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findByAgreementToken(String agreementToken);
}
