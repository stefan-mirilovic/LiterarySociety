package com.paymentconcentrator.cp.dto;

import lombok.Data;

@Data
public class BankResultDTO {
    private Long merchantOrderId;
    private Long acquirerOrderId;
    private String acquirerTimestamp;
    private Long paymentId;

    public BankResultDTO(Long merchantOrderId, Long acquirerOrderId, String acquirerTimestamp, Long paymentId) {
        this.merchantOrderId = merchantOrderId;
        this.acquirerOrderId = acquirerOrderId;
        this.acquirerTimestamp = acquirerTimestamp;
        this.paymentId = paymentId;
    }
}
