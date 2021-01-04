package com.paymentconcentrator.pcc.repository;

import com.paymentconcentrator.pcc.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByCardNumber(String cardNumber);
}
