package com.paymentconcentrator.cp.client;

import lombok.Generated;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "bank")
public interface BankClient {

	@GetMapping(value = "/api")
	public String getTest();
}
