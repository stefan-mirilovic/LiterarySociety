package com.paymentconcentrator.cp.service;

import com.paymentconcentrator.cp.dto.OrderDto;
import com.paymentconcentrator.cp.dto.GenericPaymentResponseDto;
import com.paymentconcentrator.cp.dto.RedirectDto;

public interface RequestPaymentService {

	RedirectDto createRequest(OrderDto orderDto);

	void receiveResult(GenericPaymentResponseDto dto);
}
