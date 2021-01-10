package com.paymentconcentrator.cp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantDTO {
    private Long id;
    private String email;
    private UUID merchantId;
    private String merchantPassword;
}
