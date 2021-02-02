package com.paymentconcentrator.cp.service.impl;

import com.paymentconcentrator.cp.client.GenericPaymentClient;
import com.paymentconcentrator.cp.client.PayPalClient;
import com.paymentconcentrator.cp.dto.*;
import com.paymentconcentrator.cp.enumeration.SubscriptionStatus;
import com.paymentconcentrator.cp.enumeration.TransactionStatus;
import com.paymentconcentrator.cp.model.Merchant;
import com.paymentconcentrator.cp.model.Subscription;
import com.paymentconcentrator.cp.model.Transaction;
import com.paymentconcentrator.cp.repository.MerchantRepository;
import com.paymentconcentrator.cp.repository.SubscriptionRepository;
import com.paymentconcentrator.cp.repository.TransactionRepository;
import com.paymentconcentrator.cp.service.RequestPaymentService;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.util.io.TeeInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestPaymentServiceImpl implements RequestPaymentService {

	private final TransactionRepository transactionRepository;
	private final SubscriptionRepository subscriptionRepository;
	private final MerchantRepository merchantRepository;
	private final PayPalClient payPalClient;
	private static final Logger logger = LoggerFactory.getLogger(RequestBankServiceImpl.class);

	@Override
	public RedirectDto createRequest(OrderDto orderDto) {
		UUID merchantId = UUID.fromString(orderDto.getMerchantId());
		Merchant merchant = merchantRepository.findByMerchantId(merchantId);

		Transaction transaction = new Transaction();
		transaction.setMerchant(merchant);
		transaction.setAmount(orderDto.getAmount());
		transaction.setTimestamp(LocalDateTime.now());
		transaction.setStatus(TransactionStatus.INCOMPLETE);
		transaction = transactionRepository.save(transaction);

		GenericPaymentRequestDto genericPaymentRequestDto = new GenericPaymentRequestDto();
		genericPaymentRequestDto.setAmount(orderDto.getAmount());
		genericPaymentRequestDto.setErrorUrl(orderDto.getErrorUrl());
		genericPaymentRequestDto.setSuccessUrl(orderDto.getSuccessUrl());
		genericPaymentRequestDto.setFailedUrl(orderDto.getFailedUrl());
		genericPaymentRequestDto.setMerchantId(merchantId);
		genericPaymentRequestDto.setMerchantPassword(merchant.getMerchantPassword());
		genericPaymentRequestDto.setMerchantOrderId(transaction.getId());

		GenericPaymentClient genericPaymentClient = Feign.builder()
				.encoder(new GsonEncoder())
				.target(GenericPaymentClient.class, orderDto.getPaymentUrl() + "/api/pay");

		String redirectUrl = genericPaymentClient.pay(genericPaymentRequestDto);
		RedirectDto redirectDto = new RedirectDto();
		redirectDto.setRedirectLink(redirectUrl);
		logger.info("Transaction created and forwarded to" + orderDto.getPaymentMethod() + ". DTO: " + orderDto.toString());
		return redirectDto;
	}

	@Override
	public void receiveResult(GenericPaymentResponseDto dto) {
		Transaction transaction = transactionRepository.findById(dto.getMerchantOrderId()).get();
		transaction.setStatus(TransactionStatus.COMPLETED);
		transaction.setAcquirerTimestamp(LocalDateTime.now());
		transactionRepository.save(transaction);
		logger.info("Transaction completed by " + dto.getPaymentMethod() + ". DTO: " + dto.toString());
	}

	@Override
	public RedirectDto createSubscriptionRequest(OrderDto orderDto) {
		UUID merchantId = UUID.fromString(orderDto.getMerchantId());
		Merchant merchant = merchantRepository.findByMerchantId(merchantId);

		Subscription subscription = new Subscription();
		subscription.setMerchant(merchant);
		subscription.setAmount(orderDto.getAmount());
		subscription.setTimestamp(LocalDateTime.now());
		subscription.setStatus(SubscriptionStatus.INACTIVE);
		subscription.setCycles(orderDto.getCycles());
		subscription.setFrequency(orderDto.getFrequency());
		subscription.setInterval(orderDto.getInterval());
		subscription = subscriptionRepository.save(subscription);

		SubscriptionRequestDTO genericPaymentRequestDto = new SubscriptionRequestDTO();
		genericPaymentRequestDto.setAmount(orderDto.getAmount());
		genericPaymentRequestDto.setErrorUrl(orderDto.getErrorUrl());
		genericPaymentRequestDto.setSuccessUrl(orderDto.getSuccessUrl());
		genericPaymentRequestDto.setFailedUrl(orderDto.getFailedUrl());
		genericPaymentRequestDto.setMerchantId(merchantId);
		genericPaymentRequestDto.setMerchantPassword(merchant.getMerchantPassword());
		genericPaymentRequestDto.setMerchantOrderId(subscription.getId());
		genericPaymentRequestDto.setCycles(orderDto.getCycles());
		genericPaymentRequestDto.setFrequency(orderDto.getFrequency());
		genericPaymentRequestDto.setInterval(orderDto.getInterval());


		GenericPaymentClient genericPaymentClient = Feign.builder()
				.encoder(new GsonEncoder())
				.target(GenericPaymentClient.class, orderDto.getPaymentUrl() + "/api/pay/subscription");

		String redirectUrl = genericPaymentClient.paySubscription(genericPaymentRequestDto);
		RedirectDto redirectDto = new RedirectDto();
		redirectDto.setRedirectLink(redirectUrl);
		logger.info("Subscription created and forwarded to" + orderDto.getPaymentMethod() + ". DTO: " + orderDto.toString());
		return redirectDto;
	}

	@Override
	public void receiveSubscriptionResult(GenericPaymentResponseDto dto) {
		Subscription subscription = subscriptionRepository.findById(dto.getMerchantOrderId()).get();
		subscription.setStatus(SubscriptionStatus.ACTIVE);
		subscriptionRepository.save(subscription);
		logger.info("Subscription activated by " + dto.getPaymentMethod() + ". DTO: " + dto.toString());
	}
}
