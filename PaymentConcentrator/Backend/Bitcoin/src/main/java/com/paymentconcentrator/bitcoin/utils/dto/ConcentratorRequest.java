package com.paymentconcentrator.bitcoin.utils.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ConcentratorRequest {
	private UUID merchantId;
	private String merchantPassword;
	private Double amount;
	private Long merchantOrderId;
	private LocalDateTime merchantTimestamp;
	private String successUrl;
	private String failedUrl;
	private String errorUrl;
}
