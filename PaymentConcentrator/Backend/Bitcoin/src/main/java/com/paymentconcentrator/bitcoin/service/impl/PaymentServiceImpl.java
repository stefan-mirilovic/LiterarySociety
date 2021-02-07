package com.paymentconcentrator.bitcoin.service.impl;

import com.paymentconcentrator.bitcoin.client.PaymentConcentratorClient;
import com.paymentconcentrator.bitcoin.enumeration.TransactionStatus;
import com.paymentconcentrator.bitcoin.model.Account;
import com.paymentconcentrator.bitcoin.model.Transaction;
import com.paymentconcentrator.bitcoin.repository.AccountRepository;
import com.paymentconcentrator.bitcoin.repository.TransactionRepository;
import com.paymentconcentrator.bitcoin.service.PaymentService;
import com.paymentconcentrator.bitcoin.utils.PaymentUtils;
import com.paymentconcentrator.bitcoin.utils.dto.*;
import com.paymentconcentrator.bitcoin.utils.globals.PaymentConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;
	private final PaymentConcentratorClient paymentConcentratorClient;
	private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Scheduled(fixedRate = 200000)
	public void autoCheck() {
		logger.info("Transaction check starting...");
		List<Transaction> transactions = transactionRepository.findAllByStatus(TransactionStatus.IN_PROGRESS);
		for (Transaction t: transactions) {
			if (t.getTimestamp().isBefore(LocalDateTime.now().minusMinutes(20))) {
				t.setStatus(TransactionStatus.CANCELLED);
				transactionRepository.save(t);
				logger.info("Transaction cancelled for taking too long to complete. ID: "+t.getId());
			}
		}
		logger.info("Transaction check finished");
	}

	@Override
	public String sendOrder(ConcentratorRequest concentratorRequest) {
		PreparedPaymentDto paymentDto = preparePayment(concentratorRequest);

		ResponseEntity<ApiResponseDto> response = PaymentUtils.postOrder(paymentDto);
		String paymentUrl = Objects.requireNonNull(response.getBody().getPaymentUrl());

		return paymentUrl;
	}

	@Override
	public MerchantConnectRequestDTO connectMerchant(MerchantConnectRequestDTO dto) {
		Account account = accountRepository.findByMerchantId(dto.getMerchantId());
		if (account == null) {
			account = new Account();
		}
		account = new Account();
		account.setToken(dto.getUsername());
		account.setMerchantId(dto.getMerchantId());
		account = accountRepository.save(account);
		logger.info("Account ID: "+account.getId()+" has been connected to payment concentrator and made a merchant");
		return dto;
	}

	private PreparedPaymentDto preparePayment(ConcentratorRequest request) {
		Account merchant = accountRepository.findByMerchantId(request.getMerchantId());

		PreparedPaymentDto paymentDto = new PreparedPaymentDto();
		paymentDto.setAmount(request.getAmount());
		paymentDto.setApiToken(merchant.getToken());
		paymentDto.setCancelUrl(PaymentConstants.Url.HOST + PaymentConstants.Url.CANCEL_URL + request.getMerchantOrderId());
		paymentDto.setSuccessUrl(PaymentConstants.Url.HOST + PaymentConstants.Url.SUCCESS_URL + request.getMerchantOrderId());
		paymentDto.setTitle(PaymentConstants.Info.TITLES);
		paymentDto.setRedirectUrl(request.getSuccessUrl());
		paymentDto.setPaymentId(request.getMerchantOrderId());
		paymentDto.setCurrency(PaymentConstants.Info.CURRENCY);

		//create transaction in our database
		Transaction transaction = new Transaction();
		transaction.setAmount(request.getAmount());
		transaction.setMerchantOrderId(request.getMerchantOrderId());
		transaction.setSeller(merchant);
		transaction.setStatus(TransactionStatus.IN_PROGRESS);
		transaction.setTimestamp(LocalDateTime.now());
		transaction.setSuccessUrl(request.getSuccessUrl());
		transaction.setFailedUrl(request.getFailedUrl());
		transaction.setErrorUrl(request.getErrorUrl());
		transaction = transactionRepository.save(transaction);
		logger.info("Transaction created (id: " + transaction.getId() + ", merchantOrderId: " + request.getMerchantOrderId() + "). Redirecting client to Bitcoin API.");

		return paymentDto;
	}

	@Override
	public String finishTransaction(Long paymentId) {
		BitcoinResultDto bitcoinResultDto = new BitcoinResultDto();
		bitcoinResultDto.setMerchantOrderId(paymentId);
		bitcoinResultDto.setPaymentMethod(PaymentConstants.Info.PAYMENT_METHOD);
		paymentConcentratorClient.sendResult(bitcoinResultDto);

		Transaction transaction = transactionRepository.findByMerchantOrderId(paymentId);
		transaction.setStatus(TransactionStatus.COMPLETED);
		transactionRepository.save(transaction);
		logger.info("Transaction completed (id: " + transaction.getId() + ", merchantOrderId: " + paymentId + ").");
		return transaction.getSuccessUrl();
	}

	@Override
	public String cancelTransaction(Long paymentId) {
		Transaction transaction = transactionRepository.findByMerchantOrderId(paymentId);
		transaction.setStatus(TransactionStatus.CANCELLED);
		transactionRepository.save(transaction);
		logger.info("Transaction cancelled (id: " + transaction.getId() + ", merchantOrderId: " + paymentId + ").");
		return transaction.getFailedUrl();
	}
}
