package com.paymentconcentrator.cp.service.impl;

import com.paymentconcentrator.cp.client.PayPalClient;
import com.paymentconcentrator.cp.client.PaymentClient;
import com.paymentconcentrator.cp.dto.OrderDto;
import com.paymentconcentrator.cp.dto.PayPalRequestDto;
import com.paymentconcentrator.cp.dto.PayPalResponseDto;
import com.paymentconcentrator.cp.dto.RedirectDto;
import com.paymentconcentrator.cp.enumeration.TransactionStatus;
import com.paymentconcentrator.cp.model.Merchant;
import com.paymentconcentrator.cp.model.Transaction;
import com.paymentconcentrator.cp.repository.MerchantRepository;
import com.paymentconcentrator.cp.repository.TransactionRepository;
import com.paymentconcentrator.cp.service.RequestPaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestPaymentServiceImpl implements RequestPaymentService {

	private final TransactionRepository transactionRepository;
	private final MerchantRepository merchantRepository;
	private final PayPalClient payPalClient;
	private static final Logger logger = LoggerFactory.getLogger(RequestBankServiceImpl.class);
	@Override
	public RedirectDto createRequest(OrderDto orderDto) {
		Transaction transaction = new Transaction();
		UUID merchantId = UUID.fromString(orderDto.getMerchantId());
		Merchant merchant = merchantRepository.findByMerchantId(merchantId);
		transaction.setMerchant(merchant);
		transaction.setAmount(orderDto.getAmount());
		transaction.setTimestamp(LocalDateTime.now());
		transaction.setStatus(TransactionStatus.INCOMPLETE);
	 	transaction = transactionRepository.save(transaction);

		PayPalRequestDto payPalRequestDto = new PayPalRequestDto();
		payPalRequestDto.setAmount(orderDto.getAmount());
		payPalRequestDto.setErrorUrl(orderDto.getErrorUrl());
		payPalRequestDto.setSuccessUrl(orderDto.getSuccessUrl());
		payPalRequestDto.setFailedUrl(orderDto.getFailedUrl());
		payPalRequestDto.setMerchantId(merchantId);
		payPalRequestDto.setMerchantOrderId(transaction.getId());
		payPalRequestDto.setMerchantTimestamp(transaction.getTimestamp());

		String redirectUrl = payPalClient.pay(payPalRequestDto);
		RedirectDto redirectDto = new RedirectDto();
		redirectDto.setRedirectLink(redirectUrl);
		logger.info("Transaction created and forwarded to"+orderDto.getPaymentMethod()+". DTO: " + orderDto.toString());
		return redirectDto;
	}

	@Override
	public void receiveResult(PayPalResponseDto dto) {
		Transaction transaction = transactionRepository.findById(dto.getMerchantOrderId()).get();
		transaction.setStatus(TransactionStatus.COMPLETED);
		transaction.setAcquirerTimestamp(LocalDateTime.now());
		transactionRepository.save(transaction);
		logger.info("Transaction completed by paypal. DTO: " + dto.toString());
	}
}
