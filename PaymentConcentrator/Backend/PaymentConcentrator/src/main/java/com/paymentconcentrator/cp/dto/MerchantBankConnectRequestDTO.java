package com.paymentconcentrator.cp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantBankConnectRequestDTO {
    private String number;
    private String securityCode;
    private String cardHolderName;
    private String expDate;
    private UUID merchantId;
    private String merchantPassword;
}
