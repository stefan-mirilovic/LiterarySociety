package com.paymentconcentrator.bank.dto;

import lombok.Data;

@Data
public class IssuerDetailsDTO {
    private String number;
    private String securityCode;
    private String cardHolderName;
    private String expDate;
    private Long paymentId;
}
