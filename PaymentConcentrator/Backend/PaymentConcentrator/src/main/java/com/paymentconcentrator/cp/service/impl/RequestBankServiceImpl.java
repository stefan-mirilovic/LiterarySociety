package com.paymentconcentrator.cp.service.impl;

import com.paymentconcentrator.cp.dto.BankRequestDto;
import com.paymentconcentrator.cp.dto.OrderDto;
import com.paymentconcentrator.cp.model.Merchant;
import com.paymentconcentrator.cp.repository.MerchantRepository;
import com.paymentconcentrator.cp.service.RequestBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestBankServiceImpl implements RequestBankService {

	private final MerchantRepository merchantRepository;

	@Override
	public BankRequestDto createRequest(OrderDto orderDto) {
		UUID merchantId = UUID.fromString(orderDto.getMerchantId());
		Merchant merchant = merchantRepository.findByMerchantId(merchantId);
		BankRequestDto bankRequestDto = new BankRequestDto();
		bankRequestDto.setErrorUrl("/error");
		bankRequestDto.setFailedUrl("/failed");
		bankRequestDto.setSuccessUrl("/success");
		bankRequestDto.setMerchantTimestamp(LocalDateTime.now());
		bankRequestDto.setMerchantOrderId(merchantId);
		bankRequestDto.setAmount(orderDto.getAmount());
		bankRequestDto.setMerchantId(merchant.getMerchantId());
		bankRequestDto.setMerchantPassword(merchant.getMerchantPassword());
		return bankRequestDto;
	}
}
