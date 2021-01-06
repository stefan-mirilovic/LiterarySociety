package com.paymentconcentrator.pcc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {
    private String number;
    private String securityCode;
    private String cardHolderName;
    private String expDate;
    private Long acquirerOrderId;
    private Double amount;
    private Long MerchantOrderId;
}
