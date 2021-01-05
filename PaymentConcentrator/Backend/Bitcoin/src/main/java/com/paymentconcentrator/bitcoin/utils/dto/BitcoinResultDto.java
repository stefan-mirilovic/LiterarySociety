package com.paymentconcentrator.bitcoin.utils.dto;

import lombok.Data;

@Data
public class BitcoinResultDto {
	private Long merchantOrderId;
	private String paymentId;
	private String paymentMethod;
}
