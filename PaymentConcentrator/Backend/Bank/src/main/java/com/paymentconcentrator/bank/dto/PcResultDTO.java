package com.paymentconcentrator.bank.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PcResultDTO {
    private UUID merchantOrderId;
    private Long acquirerOrderId;
    private String acquirerTimestamp;
    private Long paymentId;

    public PcResultDTO(UUID merchantOrderId, Long acquirerOrderId, String acquirerTimestamp, Long paymentId) {
        this.merchantOrderId = merchantOrderId;
        this.acquirerOrderId = acquirerOrderId;
        this.acquirerTimestamp = acquirerTimestamp;
        this.paymentId = paymentId;
    }
}
