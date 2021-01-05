package com.paymentconcentrator.bitcoin.utils.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PreparedPaymentDto {
	private String title;
	private Long paymentId;
	private String apiToken;
	private String currency;
	private String cancelUrl;
	private Double amount;
	private String successUrl;
	private String redirectUrl;
}
