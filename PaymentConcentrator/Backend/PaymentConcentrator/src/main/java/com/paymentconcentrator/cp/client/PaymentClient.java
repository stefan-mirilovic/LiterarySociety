package com.paymentconcentrator.cp.client;

import com.paymentconcentrator.cp.dto.OrderDto;
import feign.Headers;
import feign.RequestLine;

public interface PaymentClient {

	@RequestLine("POST")
	@Headers("Content-Type: application/json")
	void pay(OrderDto orderDto);
}
