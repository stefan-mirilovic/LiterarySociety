package com.paymentconcentrator.paypal.repository;

import com.paymentconcentrator.paypal.enumeration.SubscriptionStatus;
import com.paymentconcentrator.paypal.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findByAgreementToken(String agreementToken);

    List<Subscription> findByStatus(SubscriptionStatus created);
}
