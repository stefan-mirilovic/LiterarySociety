package com.paymentconcentrator.cp.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderDto {
	private Double amount;
	private String merchantId;
	private String paymentMethod;
	private String successUrl;
	private String failedUrl;
	private String errorUrl;
}
