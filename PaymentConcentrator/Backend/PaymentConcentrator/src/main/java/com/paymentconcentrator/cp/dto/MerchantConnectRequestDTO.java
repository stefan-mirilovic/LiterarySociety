package com.paymentconcentrator.cp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantConnectRequestDTO {
    private String username;
    private String password;
    private UUID merchantId;
    private String merchantPassword;
}
