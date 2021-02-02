package com.paymentconcentrator.paypal.service;

import com.paymentconcentrator.paypal.dto.MerchantConnectRequestDTO;
import com.paymentconcentrator.paypal.dto.PayPalRequestDto;
import com.paymentconcentrator.paypal.dto.SubscriptionRequestDTO;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public interface PayPalService {

	/**
	 * Method for redirecting to pay pal app
	 * @param palRequestDto
	 * @return
	 * @throws PayPalRESTException
	 */
	Payment redirectUrl(PayPalRequestDto palRequestDto) throws PayPalRESTException;

	String redirectLink(Payment payment);

	String executePayment(String paymentId, String payerId, Long merchantId) throws PayPalRESTException;

    MerchantConnectRequestDTO connectMerchant(MerchantConnectRequestDTO dto) throws Exception;

    String createBillingPlan(SubscriptionRequestDTO subscriptionRequestDto) throws PayPalRESTException, MalformedURLException, UnsupportedEncodingException;

	String executeBilling(String token, Long merchantId) throws PayPalRESTException;

	String cancelBilling(String token, Long merchantOrderId);

	String cancelPayment(String paymentId, String payerId, Long merchantOrder);
}
