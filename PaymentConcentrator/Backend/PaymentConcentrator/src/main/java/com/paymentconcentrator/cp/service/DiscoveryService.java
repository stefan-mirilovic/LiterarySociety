package com.paymentconcentrator.cp.service;

import com.paymentconcentrator.cp.dto.PaymentDto;

import java.util.List;

public interface DiscoveryService {

	List<PaymentDto> discoveryPayments();

	String getServicePort(String serviceName);
}
