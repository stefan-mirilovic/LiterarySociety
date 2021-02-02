package com.paymentconcentrator.cp.client;

import com.paymentconcentrator.cp.dto.GenericPaymentRequestDto;
import com.paymentconcentrator.cp.dto.MerchantBankConnectRequestDTO;
import com.paymentconcentrator.cp.dto.MerchantConnectRequestDTO;
import com.paymentconcentrator.cp.dto.SubscriptionRequestDTO;
import feign.Headers;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
public interface GenericPaymentClient {

	@RequestLine("POST")
	@RequestMapping
	@Headers("Content-Type: application/json")
	String pay(@RequestBody GenericPaymentRequestDto orderDto);

	@RequestLine("POST")
	@RequestMapping
	@Headers("Content-Type: application/json")
	String paySubscription(@RequestBody SubscriptionRequestDTO orderDto);

	@RequestLine("POST")
	@RequestMapping
	@Headers("Content-Type: application/json")
	MerchantConnectRequestDTO forwardMerchant(MerchantConnectRequestDTO dto);
}
