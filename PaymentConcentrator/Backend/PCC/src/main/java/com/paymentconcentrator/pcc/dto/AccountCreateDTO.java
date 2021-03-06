package com.paymentconcentrator.pcc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateDTO {
    private String cardNumber;
    private String bankUrl;
}
