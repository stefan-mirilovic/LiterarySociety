package com.paymentconcentrator.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PCCAccountCreateDTO {
    private String cardNumber;
    private String bankUrl;
}
