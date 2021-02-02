package com.paymentconcentrator.paypal.client;

import com.paymentconcentrator.paypal.dto.PayPalRequestDto;
import com.paymentconcentrator.paypal.dto.PayPalResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "payment-concentrator")
public interface PaymentConcentratorClient {

	@PostMapping(value = "/api/payment/type/result")
	public void sendResult(PayPalResultDto payPalResultDto);

	@PostMapping(value = "/api/payment/type/subscription/result")
	public void sendSubscriptionResult(PayPalResultDto payPalResultDto);
	
}
