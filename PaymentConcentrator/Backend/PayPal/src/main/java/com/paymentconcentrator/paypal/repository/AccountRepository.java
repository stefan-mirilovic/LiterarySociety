package com.paymentconcentrator.paypal.repository;

import com.paymentconcentrator.paypal.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, Long> {
	Account findByMerchantId(UUID merchantId);

	Account findByClientId(String clientId);
}
