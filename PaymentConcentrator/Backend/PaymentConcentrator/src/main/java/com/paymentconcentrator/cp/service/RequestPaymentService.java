package com.paymentconcentrator.cp.service;

import com.paymentconcentrator.cp.client.PayPalClient;
import com.paymentconcentrator.cp.client.PaymentClient;
import com.paymentconcentrator.cp.dto.BankResultDTO;
import com.paymentconcentrator.cp.dto.OrderDto;
import com.paymentconcentrator.cp.dto.PayPalResponseDto;
import com.paymentconcentrator.cp.dto.RedirectDto;

public interface RequestPaymentService {

	RedirectDto createRequest(OrderDto orderDto);

	void receiveResult(PayPalResponseDto dto);
}
