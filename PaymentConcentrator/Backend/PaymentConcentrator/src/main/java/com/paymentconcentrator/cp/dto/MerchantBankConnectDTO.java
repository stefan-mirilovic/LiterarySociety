package com.paymentconcentrator.cp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantBankConnectDTO {
    private String number;
    private String securityCode;
    private String cardHolderName;
    private String expDate;
    private String name;
    private String url;
}
