package com.paymentconcentrator.bitcoin.service.impl;

import com.paymentconcentrator.bitcoin.model.Account;
import com.paymentconcentrator.bitcoin.repository.AccountRepository;
import com.paymentconcentrator.bitcoin.service.PaymentService;
import com.paymentconcentrator.bitcoin.utils.PaymentUtils;
import com.paymentconcentrator.bitcoin.utils.dto.*;
import com.paymentconcentrator.bitcoin.utils.globals.PaymentConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final AccountRepository accountRepository;

	@Override
	public String sendOrder(ConcentratorRequest concentratorRequest) {
		PreparedPaymentDto paymentDto = preparePayment(concentratorRequest);

		ResponseEntity<ApiResponseDto> response = PaymentUtils.postOrder(paymentDto);
		String paymentUrl = Objects.requireNonNull(response.getBody().getPaymentUrl());

		return paymentUrl;
	}

	@Override
	public MerchantConnectRequestDTO connectMerchant(MerchantConnectRequestDTO dto) {
		Account account = new Account();
		account.setToken(dto.getUsername());
		account.setMerchantId(dto.getMerchantId());
		accountRepository.save(account);
		return dto;
	}

	private PreparedPaymentDto preparePayment(ConcentratorRequest request) {
		Account merchant = accountRepository.findByMerchantId(request.getMerchantId());

		PreparedPaymentDto paymentDto = new PreparedPaymentDto();
		paymentDto.setAmount(request.getAmount());
		paymentDto.setApiToken(merchant.getToken());
		paymentDto.setCancelUrl(request.getFailedUrl());
		paymentDto.setSuccessUrl(PaymentConstants.Url.SUCCESS_URL + request.getMerchantOrderId());
		paymentDto.setTitle(PaymentConstants.Info.TITLES);
		paymentDto.setRedirectUrl(request.getSuccessUrl());
		paymentDto.setPaymentId(request.getMerchantOrderId());
		paymentDto.setCurrency(PaymentConstants.Info.CURRENCY);

		return paymentDto;
	}
}
