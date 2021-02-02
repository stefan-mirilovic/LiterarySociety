package com.paymentconcentrator.cp.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SubscriptionRequestDTO {
    private UUID merchantId;
    private String merchantPassword;
    private Double amount;
    private Long merchantOrderId;
    private LocalDateTime merchantTimestamp;
    private String successUrl;
    private String failedUrl;
    private String errorUrl;
    private String frequency;
    private String interval;
    private Integer cycles;
}
