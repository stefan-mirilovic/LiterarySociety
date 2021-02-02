package com.paymentconcentrator.cp.repository;

import com.paymentconcentrator.cp.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
