package com.paymentconcentrator.cp.dto;

import lombok.Data;

@Data
public class GenericPaymentResponseDto {
	private Long merchantOrderId;
	private Long acquirerOrderId;
	private String acquirerTimestamp;
	private String paymentMethod;
	private Long paymentId;
}
