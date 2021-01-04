package com.paymentconcentrator.paypal.service.impl;

import com.paymentconcentrator.paypal.dto.PayPalRequestDto;
import com.paymentconcentrator.paypal.dto.PayPalResultDto;
import com.paymentconcentrator.paypal.model.Account;
import com.paymentconcentrator.paypal.repository.AccountRepository;
import com.paymentconcentrator.paypal.service.PayPalService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PayPalServiceImpl implements PayPalService {

	private final AccountRepository accountRepository;
	private APIContext apiContext;


	@Override
	public Payment redirectUrl(PayPalRequestDto payPalRequestDto) throws PayPalRESTException {
		Double total;
		RedirectUrls redirectUrls = new RedirectUrls();
		List<Transaction> transactionList = new ArrayList<>();
		Account account = accountRepository.findByMerchantId(payPalRequestDto.getMerchantId());

		Map<String, String> configMap = new HashMap<>();
		configMap.put("mode", "sandbox");

		Amount amount = new Amount();
		amount.setCurrency("USD");
		total = new BigDecimal(payPalRequestDto.getAmount()).setScale(2, RoundingMode.HALF_UP).doubleValue();
		amount.setTotal(String.format("%.2f", total));

		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setAmount(amount);
		transactionList.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setTransactions(transactionList);

		redirectUrls.setCancelUrl(payPalRequestDto.getFailedUrl());
		String merchantOrderId= String.valueOf(payPalRequestDto.getMerchantOrderId());
		redirectUrls.setReturnUrl("http://localhost:8662/paypal/api/pay/success/"+ merchantOrderId);
		payment.setRedirectUrls(redirectUrls);

		OAuthTokenCredential oAuthToken = new OAuthTokenCredential(account.getClientId(), account.getClientSecret(), configMap);
	 	apiContext = new APIContext(oAuthToken.getAccessToken());
		apiContext.setConfigurationMap(configMap);
		Payment paymentCreated=payment.create(apiContext);
		return paymentCreated;
	}

	@Override
	public String redirectLink(Payment payment) {
		String retVal = "";
		for (Links link : payment.getLinks()) {
			if (link.getRel().equals("approval_url")) {
				retVal = link.getHref();
			}
		}
		return retVal;
	}

	@Override
	public PayPalResultDto executePayment(String paymentId, String payerId, Long merchantId) throws PayPalRESTException {
		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(payerId);
		payment.execute(apiContext, paymentExecution);
		PayPalResultDto payPalResultDto = new PayPalResultDto();
		payPalResultDto.setMerchantOrderId(merchantId);
		payPalResultDto.setPaymentMethod("paypal");
		return payPalResultDto;
	}
}
