package com.paymentconcentrator.paypal.service;

import com.paymentconcentrator.paypal.dto.PayPalRequestDto;
import com.paymentconcentrator.paypal.dto.PayPalResultDto;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PayPalService {

	/**
	 * Method for redirecting to pay pal app
	 * @param palRequestDto
	 * @return
	 * @throws PayPalRESTException
	 */
	Payment redirectUrl(PayPalRequestDto palRequestDto) throws PayPalRESTException;

	String redirectLink(Payment payment);

	PayPalResultDto executePayment(String paymentId, String payerId, Long merchantId) throws PayPalRESTException;
}
