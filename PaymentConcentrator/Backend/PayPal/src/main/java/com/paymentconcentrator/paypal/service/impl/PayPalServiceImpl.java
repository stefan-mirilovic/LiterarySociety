package com.paymentconcentrator.paypal.service.impl;

import com.paymentconcentrator.paypal.client.PaymentConcentratorClient;
import com.paymentconcentrator.paypal.dto.MerchantConnectRequestDTO;
import com.paymentconcentrator.paypal.dto.PayPalRequestDto;
import com.paymentconcentrator.paypal.dto.PayPalResultDto;
import com.paymentconcentrator.paypal.dto.SubscriptionRequestDTO;
import com.paymentconcentrator.paypal.enumeration.SubscriptionStatus;
import com.paymentconcentrator.paypal.enumeration.TransactionStatus;
import com.paymentconcentrator.paypal.model.Account;
import com.paymentconcentrator.paypal.model.Subscription;
import com.paymentconcentrator.paypal.repository.AccountRepository;
import com.paymentconcentrator.paypal.repository.SubscriptionRepository;
import com.paymentconcentrator.paypal.repository.TransactionRepository;
import com.paymentconcentrator.paypal.service.PayPalService;
import com.paypal.api.payments.*;
import com.paypal.api.payments.Currency;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PayPalServiceImpl implements PayPalService {

	private final AccountRepository accountRepository;
	private final SubscriptionRepository subscriptionRepository;
	private final TransactionRepository transactionRepository;
	private APIContext apiContext;
	private final PaymentConcentratorClient paymentConcentratorClient;

	@Scheduled(fixedRate = 200000)
	public void autoCheck() {
		List<com.paymentconcentrator.paypal.model.Transaction> transactions = transactionRepository.findByStatus(TransactionStatus.IN_PROGRESS);
		for (com.paymentconcentrator.paypal.model.Transaction t: transactions) {
			if (t.getTimestamp().isBefore(LocalDateTime.now().minusMinutes(20))) {
				t.setStatus(TransactionStatus.CANCELLED);
				transactionRepository.save(t);
			}
		}
		List<Subscription> subscriptions = subscriptionRepository.findByStatus(SubscriptionStatus.CREATED);
		for (Subscription s: subscriptions) {
			if (s.getTimestamp().isBefore(LocalDateTime.now().minusMinutes(20))) {
				s.setStatus(SubscriptionStatus.CANCELLED);
				subscriptionRepository.save(s);
			}
		}
	}

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

		String merchantOrderId= String.valueOf(payPalRequestDto.getMerchantOrderId());
		redirectUrls.setCancelUrl(payPalRequestDto.getFailedUrl());
		redirectUrls.setReturnUrl("http://localhost:8662/paypal/api/pay/success/"+ merchantOrderId);
		payment.setRedirectUrls(redirectUrls);

		OAuthTokenCredential oAuthToken = new OAuthTokenCredential(account.getClientId(), account.getClientSecret(), configMap);
	 	apiContext = new APIContext(oAuthToken.getAccessToken());
		apiContext.setConfigurationMap(configMap);
		Payment paymentCreated=payment.create(apiContext);

		//create transaction in our database
		com.paymentconcentrator.paypal.model.Transaction myTransaction = new com.paymentconcentrator.paypal.model.Transaction();
		myTransaction.setPaymentId(paymentCreated.getId());
		myTransaction.setAmount(payPalRequestDto.getAmount());
		myTransaction.setMerchantOrderId(payPalRequestDto.getMerchantOrderId());
		myTransaction.setSeller(account);
		myTransaction.setStatus(TransactionStatus.IN_PROGRESS);
		myTransaction.setSuccessUrl(payPalRequestDto.getSuccessUrl());
		myTransaction.setFailedUrl(payPalRequestDto.getFailedUrl());
		myTransaction.setErrorUrl(payPalRequestDto.getErrorUrl());
		myTransaction.setTimestamp(LocalDateTime.now());
		transactionRepository.save(myTransaction);
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
	public String executePayment(String paymentId, String payerId, Long merchantId) throws PayPalRESTException {
		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(payerId);
		payment.execute(apiContext, paymentExecution);
		PayPalResultDto payPalResultDto = new PayPalResultDto();
		payPalResultDto.setMerchantOrderId(merchantId);
		payPalResultDto.setPaymentMethod("paypal");
		paymentConcentratorClient.sendResult(payPalResultDto);

		//update transaction in our database
		com.paymentconcentrator.paypal.model.Transaction transaction = transactionRepository.findByPaymentId(paymentId);
		transaction.setStatus(TransactionStatus.COMPLETED);
		transactionRepository.save(transaction);

		return transaction.getSuccessUrl();
	}

	@Override
	public String cancelPayment(String paymentId, String payerId, Long merchantOrder) {
		com.paymentconcentrator.paypal.model.Transaction transaction = transactionRepository.findByPaymentId(paymentId);
		transaction.setStatus(TransactionStatus.CANCELLED);
		transactionRepository.save(transaction);
		return transaction.getFailedUrl();
	}

	@Override
	public MerchantConnectRequestDTO connectMerchant(MerchantConnectRequestDTO dto) {
		Account account = accountRepository.findByMerchantId(dto.getMerchantId());
		if (account == null) {
			account = new Account();
		}
		account.setClientId(dto.getUsername());
		account.setClientSecret(dto.getPassword());
		account.setMerchantId(dto.getMerchantId());
		accountRepository.save(account);
		return dto;
	}

	public String createBillingPlan(SubscriptionRequestDTO request) throws PayPalRESTException, MalformedURLException, UnsupportedEncodingException {
		Account account = accountRepository.findByMerchantId(request.getMerchantId());
		Map<String, String> configMap = new HashMap<>();
		configMap.put("mode", "sandbox");
		OAuthTokenCredential oAuthToken = new OAuthTokenCredential(account.getClientId(), account.getClientSecret(), configMap);
		apiContext = new APIContext(oAuthToken.getAccessToken());
		apiContext.setConfigurationMap(configMap);
		Plan plan = new Plan();
		plan.setName("Literary Society Membership Plan");
		plan.setDescription("Template creation.");
		plan.setType("infinite");
		Plan createdPlan = null;
		PaymentDefinition paymentDefinition = new PaymentDefinition();

		Double total = BigDecimal.valueOf(request.getAmount()).setScale(2, RoundingMode.HALF_UP).doubleValue();
		Currency amount = new Currency("USD", String.valueOf(total));

		paymentDefinition.setChargeModels(List.of(new ChargeModels("SHIPPING", amount)));
		paymentDefinition.setName("Regular Payments");
		paymentDefinition.setType("REGULAR");
		paymentDefinition.setAmount(amount);
		paymentDefinition.setCycles(String.valueOf(request.getCycles()));
		paymentDefinition.setFrequency(request.getFrequency());
		paymentDefinition.setFrequencyInterval(request.getInterval());
		List<PaymentDefinition> paymentDefinitions = new ArrayList<>();
		paymentDefinitions.add(paymentDefinition);
		plan.setPaymentDefinitions(paymentDefinitions);
		plan.setMerchantPreferences(merchantPreferences(amount, request));

		try {
			// Create payment
			createdPlan = plan.create(apiContext);

			// Set up plan activate PATCH request
			List<Patch> patchRequestList = new ArrayList<Patch>();
			Map<String, String> value = new HashMap<String, String>();
			value.put("state", "ACTIVE");

			// Create update object to activate plan
			Patch patch = new Patch();
			patch.setPath("/");
			patch.setValue(value);
			patch.setOp("replace");
			patchRequestList.add(patch);

			// Activate plan
			createdPlan.update(apiContext, patchRequestList);
		} catch (PayPalRESTException e) {
			System.err.println(e.getDetails());
		}

		// Create new agreement
		Agreement agreement = new Agreement();
		agreement.setName("Literary Society Membership Agreement");
		agreement.setDescription("Starting new subscription agreement");
		agreement.setStartDate(generateTomorrowDateTime());

		Plan plan1 = new Plan();
		plan1.setId(createdPlan.getId());
		agreement.setPlan(plan1);

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");
		agreement.setPayer(payer);
		String retVal = null;
		agreement = agreement.create(apiContext);

		//create subscription in our database
		Subscription subscription = new Subscription();
		subscription.setAgreementToken(agreement.getToken());
		subscription.setAmount(request.getAmount());
		subscription.setCycles(request.getCycles());
		subscription.setInterval(request.getInterval());
		subscription.setFrequency(request.getFrequency());
		subscription.setMerchantOrderId(request.getMerchantOrderId());
		subscription.setSeller(account);
		subscription.setStatus(SubscriptionStatus.CREATED);
		subscription.setSuccessUrl(request.getSuccessUrl());
		subscription.setFailedUrl(request.getFailedUrl());
		subscription.setErrorUrl(request.getErrorUrl());
		subscription.setTimestamp(LocalDateTime.now());
		subscriptionRepository.save(subscription);

		for (Links links : agreement.getLinks()) {
			if ("approval_url".equals(links.getRel())) {
				URL url = new URL(links.getHref());

				//REDIRECT USER TO url
				retVal = url.toString();

				break;
			}
		}

		return retVal;
	}

	String generateTomorrowDateTime() {
		/*Date dateNow = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dateNow);
		c.add(Calendar.DATE, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		//agreement.setStartDate("2020-06-17T9:45:04Z");
		return sdf.format(dateNow);*/
		return LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")).replace(" ", "T") + "Z";
	}

	static MerchantPreferences merchantPreferences(Currency currency, SubscriptionRequestDTO dto) {
		MerchantPreferences merchantPreferences = new MerchantPreferences();

		merchantPreferences.setSetupFee(currency);
		merchantPreferences.setCancelUrl("http://localhost:8662/paypal/api/pay/subscription/cancel/"+ dto.getMerchantOrderId());
		merchantPreferences.setReturnUrl("http://localhost:8662/paypal/api/pay/subscription/success/"+ dto.getMerchantOrderId());
		merchantPreferences.setMaxFailAttempts("0");
		merchantPreferences.setAutoBillAmount("YES");
		merchantPreferences.setInitialFailAmountAction("CONTINUE");

		return merchantPreferences;
	}

	private APIContext generateApiContext(Account account) throws PayPalRESTException {
		Map<String, String> configMap = new HashMap<>();
		configMap.put("mode", "sandbox");
		OAuthTokenCredential oAuthToken = new OAuthTokenCredential(account.getClientId(), account.getClientSecret(), configMap);
		apiContext = new APIContext(oAuthToken.getAccessToken());
		apiContext.setConfigurationMap(configMap);
		return apiContext;
	}

	@Override
	public String executeBilling(String token, Long merchantOrderId) throws PayPalRESTException {
		Agreement agreement =  new Agreement();
		agreement.setToken(token);

		Agreement activeAgreement = agreement.execute(apiContext, agreement.getToken());

		PayPalResultDto payPalResultDto = new PayPalResultDto();
		payPalResultDto.setMerchantOrderId(merchantOrderId);
		payPalResultDto.setPaymentMethod("paypal");
		paymentConcentratorClient.sendSubscriptionResult(payPalResultDto);
		Subscription subscription = subscriptionRepository.findByAgreementToken(token);
		subscription.setStatus(SubscriptionStatus.ACTIVE);
		subscriptionRepository.save(subscription);
		return subscription.getSuccessUrl();
	}

	@Override
	public String cancelBilling(String token, Long merchantOrderId) {
		Subscription subscription = subscriptionRepository.findByAgreementToken(token);
		subscription.setStatus(SubscriptionStatus.CANCELLED);
		subscriptionRepository.save(subscription);
		return subscription.getFailedUrl();
	}
}
