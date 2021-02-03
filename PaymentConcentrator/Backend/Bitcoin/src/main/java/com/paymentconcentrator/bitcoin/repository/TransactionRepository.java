package com.paymentconcentrator.bitcoin.repository;

import com.paymentconcentrator.bitcoin.enumeration.TransactionStatus;
import com.paymentconcentrator.bitcoin.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByMerchantOrderId(Long merchantOrderId);

    List<Transaction> findAllByStatus(TransactionStatus status);
}
