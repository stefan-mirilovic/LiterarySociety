package com.paymentconcentrator.cp.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class BankResultDTO {
    private UUID merchantOrderId;
    private Long acquirerOrderId;
    private String acquirerTimestamp;
    private Long paymentId;

    public BankResultDTO(UUID merchantOrderId, Long acquirerOrderId, String acquirerTimestamp, Long paymentId) {
        this.merchantOrderId = merchantOrderId;
        this.acquirerOrderId = acquirerOrderId;
        this.acquirerTimestamp = acquirerTimestamp;
        this.paymentId = paymentId;
    }
}
