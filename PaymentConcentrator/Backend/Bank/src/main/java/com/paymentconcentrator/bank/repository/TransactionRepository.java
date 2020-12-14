package com.paymentconcentrator.bank.repository;

import com.paymentconcentrator.bank.enumeration.TransactionType;
import com.paymentconcentrator.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = "select * from transaction t where t.payment_id = ?1 and t.type = ?2", nativeQuery = true)
    Transaction findByPaymentIdAndType(Long paymentId, TransactionType type);

    List<Transaction> findByPaymentId(Long paymentId);

    Transaction findFirstByPaymentIdOrderByType(Long paymentId);

    Transaction findFirstByTypeOrderByPaymentIdDesc(TransactionType type);
}
