package com.paymentconcentrator.cp.repository;

import com.paymentconcentrator.cp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
