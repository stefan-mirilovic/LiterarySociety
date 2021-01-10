package com.paymentconcentrator.cp.client;

import com.paymentconcentrator.cp.dto.BankRequestDto;
import com.paymentconcentrator.cp.dto.BankResponseDTO;
import com.paymentconcentrator.cp.dto.MerchantBankConnectRequestDTO;
import feign.Headers;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestMapping;

public interface BankClient {

	@RequestLine("POST")
	@RequestMapping
	@Headers("Content-Type: application/json")
	BankResponseDTO forwardToBank(BankRequestDto bankRequestDto);

	@RequestLine("POST")
	@RequestMapping
	@Headers("Content-Type: application/json")
	MerchantBankConnectRequestDTO forwardMerchantToBank(MerchantBankConnectRequestDTO dto);
}
