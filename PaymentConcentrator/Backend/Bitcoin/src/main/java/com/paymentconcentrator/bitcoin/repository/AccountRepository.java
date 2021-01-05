package com.paymentconcentrator.bitcoin.repository;

import com.paymentconcentrator.bitcoin.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, Long> {
	Account findByMerchantId(UUID merchantId);
}
