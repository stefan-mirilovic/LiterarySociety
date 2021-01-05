package com.paymentconcentrator.cp.service.impl;

import com.paymentconcentrator.cp.client.BankClient;
import com.paymentconcentrator.cp.dto.BankRequestDto;
import com.paymentconcentrator.cp.dto.BankResponseDTO;
import com.paymentconcentrator.cp.dto.BankResultDTO;
import com.paymentconcentrator.cp.dto.OrderDto;
import com.paymentconcentrator.cp.enumeration.TransactionStatus;
import com.paymentconcentrator.cp.model.Merchant;
import com.paymentconcentrator.cp.model.Transaction;
import com.paymentconcentrator.cp.repository.MerchantRepository;
import com.paymentconcentrator.cp.repository.TransactionRepository;
import com.paymentconcentrator.cp.service.RequestBankService;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestBankServiceImpl implements RequestBankService {

	private final MerchantRepository merchantRepository;
	private final TransactionRepository transactionRepository;
	//private final BankClient bankClient;
	private static final Logger logger = LoggerFactory.getLogger(RequestBankServiceImpl.class);

	@Override
	public BankResponseDTO createRequest(OrderDto orderDto) {
		UUID merchantId = UUID.fromString(orderDto.getMerchantId());
		Merchant merchant = merchantRepository.findByMerchantId(merchantId);
		Transaction transaction = new Transaction(0L, LocalDateTime.now(), orderDto.getAmount(), TransactionStatus.INCOMPLETE,
				merchant, null, null, null);
		transaction = transactionRepository.save(transaction);
		BankRequestDto bankRequestDto = new BankRequestDto();
		bankRequestDto.setErrorUrl(orderDto.getErrorUrl());
		bankRequestDto.setFailedUrl(orderDto.getFailedUrl());
		bankRequestDto.setSuccessUrl(orderDto.getSuccessUrl());
		bankRequestDto.setMerchantTimestamp(transaction.getTimestamp().toString());
		bankRequestDto.setMerchantOrderId(transaction.getId());
		bankRequestDto.setAmount(orderDto.getAmount());
		bankRequestDto.setMerchantId(merchant.getMerchantId());
		bankRequestDto.setMerchantPassword(merchant.getMerchantPassword());
		BankClient bankClient = Feign.builder()
				.encoder(new GsonEncoder())
				.decoder(new GsonDecoder())
				.target(BankClient.class, merchant.getBankUrl() + "/api");
		BankResponseDTO response = bankClient.forwardToBank(bankRequestDto);

		logger.info("Transaction created and forwarded to bank. DTO: " + bankRequestDto.toString());
		return response;
	}

	@Override
	public void receiveResult(BankResultDTO dto) {
		Transaction transaction = transactionRepository.findById(dto.getMerchantOrderId()).get();
		transaction.setStatus(TransactionStatus.COMPLETED);
		transaction.setAcquirerOrderId(dto.getAcquirerOrderId());
		transaction.setAcquirerTimestamp(LocalDateTime.parse(dto.getAcquirerTimestamp()));
		transaction.setPaymentId(dto.getPaymentId());
		transactionRepository.save(transaction);
		logger.info("Transaction completed by bank. DTO: " + dto.toString());
	}


}
