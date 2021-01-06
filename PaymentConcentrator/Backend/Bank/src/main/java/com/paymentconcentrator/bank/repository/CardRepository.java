package com.paymentconcentrator.bank.repository;

import com.paymentconcentrator.bank.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CardRepository extends JpaRepository<Card, Long> {

    Card findByNumber(String number);

    @Query(value = "select coalesce(max(id), 0) FROM Account")
    public Long getMaxId();
}
