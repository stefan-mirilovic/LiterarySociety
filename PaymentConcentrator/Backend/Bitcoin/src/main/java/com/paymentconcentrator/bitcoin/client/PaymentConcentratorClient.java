package com.paymentconcentrator.bitcoin.client;

import com.paymentconcentrator.bitcoin.utils.dto.BitcoinResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "payment-concentrator")
public interface PaymentConcentratorClient {

	@PostMapping(value = "/api/payment/type/result")
	public void sendResult(BitcoinResultDto payPalResultDto);

}
