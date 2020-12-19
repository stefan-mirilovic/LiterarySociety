package com.paymentconcentrator.cp.client;

import com.paymentconcentrator.cp.dto.BankRequestDto;
import com.paymentconcentrator.cp.dto.BankResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "bank")
public interface BankClient {

	@PostMapping(value = "/api")
	BankResponseDTO getBank(BankRequestDto bankRequestDto);
}
