package com.paymentconcentrator.cp.client;

import com.paymentconcentrator.cp.dto.BankRequestDto;
import lombok.Generated;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "bank")
public interface BankClient {

	@PostMapping(value = "/api")
	public void getBank(BankRequestDto bankRequestDto);
}
