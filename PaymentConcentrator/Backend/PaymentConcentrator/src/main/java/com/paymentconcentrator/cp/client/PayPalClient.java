package com.paymentconcentrator.cp.client;

import com.paymentconcentrator.cp.dto.GenericPaymentRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paypal")
public interface PayPalClient {

	@PostMapping(value = "/api/pay")
	String pay(@RequestBody GenericPaymentRequestDto orderDto);
}
