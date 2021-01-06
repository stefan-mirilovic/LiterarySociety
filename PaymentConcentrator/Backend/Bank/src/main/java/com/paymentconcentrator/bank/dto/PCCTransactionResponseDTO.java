package com.paymentconcentrator.bank.dto;

import com.paymentconcentrator.bank.enumeration.PCCResultType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PCCTransactionResponseDTO {
    private PCCResultType resultType;
    private Long issuerOrderId;
    private String issuerTimestamp;
}
