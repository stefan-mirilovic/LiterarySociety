package com.paymentconcentrator.bitcoin.service;

import com.paymentconcentrator.bitcoin.utils.dto.ConcentratorRequest;
import com.paymentconcentrator.bitcoin.utils.dto.PaymentUrlDto;
import com.paymentconcentrator.bitcoin.utils.dto.PreparedPaymentDto;

public interface PaymentService{

	/**
	 * Method that receives request from KP and sends POST request to CoinGate API.
	 * @param concentratorRequest - contains information about merchant, amount and redirect url
	 * @return url where user can finish payment.
	 */
	 String sendOrder(ConcentratorRequest concentratorRequest);




}
