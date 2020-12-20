package com.paymentconcentrator.cp.dto;

import lombok.Data;

@Data
public class PayPalResponseDto {
	private Long merchantOrderId;
	private Long acquirerOrderId;
	private String acquirerTimestamp;
	private Long paymentId;
}
