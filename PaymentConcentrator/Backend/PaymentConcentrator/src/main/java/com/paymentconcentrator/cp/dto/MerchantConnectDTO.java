package com.paymentconcentrator.cp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantConnectDTO {
    private String username;
    private String password;
    private String name;
    private String url;
}
