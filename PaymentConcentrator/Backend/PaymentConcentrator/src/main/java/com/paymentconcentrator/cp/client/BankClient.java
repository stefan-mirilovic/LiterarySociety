package com.paymentconcentrator.cp.client;

import com.paymentconcentrator.cp.dto.BankRequestDto;
import com.paymentconcentrator.cp.dto.BankResponseDTO;
import feign.Headers;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestMapping;

//@FeignClient(name = "bank")
public interface BankClient {

	//@PostMapping(value = "/api")
	@RequestLine("POST")
	@RequestMapping
	@Headers("Content-Type: application/json")
	BankResponseDTO forwardToBank(BankRequestDto bankRequestDto);
}
