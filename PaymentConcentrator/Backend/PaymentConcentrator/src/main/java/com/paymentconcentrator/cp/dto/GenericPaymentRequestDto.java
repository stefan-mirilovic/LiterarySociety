package com.paymentconcentrator.cp.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class GenericPaymentRequestDto implements Serializable {
	private UUID merchantId;
	private String merchantPassword;
	private Double amount;
	private Long merchantOrderId;
	private String successUrl;
	private String failedUrl;
	private String errorUrl;
}
