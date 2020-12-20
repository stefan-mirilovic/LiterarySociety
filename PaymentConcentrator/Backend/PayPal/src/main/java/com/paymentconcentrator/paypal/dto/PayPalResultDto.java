package com.paymentconcentrator.paypal.dto;

import lombok.Data;

@Data
public class PayPalResultDto {
	private Long merchantOrderId;
	private String paymentId;
}
