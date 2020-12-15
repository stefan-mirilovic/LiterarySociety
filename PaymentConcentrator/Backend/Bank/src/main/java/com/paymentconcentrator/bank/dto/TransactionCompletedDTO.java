package com.paymentconcentrator.bank.dto;

import lombok.Data;

@Data
public class TransactionCompletedDTO {
    private String url;

    public TransactionCompletedDTO(String url) {
        this.url = url;
    }
}
