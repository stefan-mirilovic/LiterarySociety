package com.paymentconcentrator.bank.dto;

import lombok.Data;

@Data
public class PcResultDTO {
    private Long merchantOrderId;
    private Long acquirerOrderId;
    private String acquirerTimestamp;
    private Long paymentId;

    public PcResultDTO(Long merchantOrderId, Long acquirerOrderId, String acquirerTimestamp, Long paymentId) {
        this.merchantOrderId = merchantOrderId;
        this.acquirerOrderId = acquirerOrderId;
        this.acquirerTimestamp = acquirerTimestamp;
        this.paymentId = paymentId;
    }
}
