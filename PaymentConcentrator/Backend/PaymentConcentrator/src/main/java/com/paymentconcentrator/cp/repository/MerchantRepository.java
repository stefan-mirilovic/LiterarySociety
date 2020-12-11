package com.paymentconcentrator.cp.repository;

import com.paymentconcentrator.cp.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
	Merchant findByMerchantId(UUID merchantId);
}
