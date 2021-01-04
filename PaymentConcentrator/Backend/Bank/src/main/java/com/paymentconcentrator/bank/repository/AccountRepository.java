package com.paymentconcentrator.bank.repository;

import com.paymentconcentrator.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByMerchantId(UUID merchantId);

    @Query(value = "select coalesce(max(id), 0) FROM Account")
    public Long getMaxId();
}
