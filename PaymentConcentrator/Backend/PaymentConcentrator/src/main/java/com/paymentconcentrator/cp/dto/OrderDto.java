package com.paymentconcentrator.cp.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderDto {
	private Double amount;
	private String merchantOrderId;
	private String merchantId;
	private String paymentMethod;
}
