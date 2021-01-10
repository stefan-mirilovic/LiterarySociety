package com.paymentconcentrator.cp.repository;

import com.paymentconcentrator.cp.model.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTypeRepository extends JpaRepository<PaymentType, Long> {
    PaymentType findByName(String name);
}
