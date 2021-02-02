package com.paymentconcentrator.paypal.repository;

import com.paymentconcentrator.paypal.enumeration.TransactionStatus;
import com.paymentconcentrator.paypal.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByPaymentId(String paymentId);
    List<Transaction> findByStatus(TransactionStatus status);
}
