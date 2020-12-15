package com.paymentconcentrator.bank.repository;

import com.paymentconcentrator.bank.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

    Card findByNumber(String number);
}
