package com.paymentconcentrator.pcc.dto;

import com.paymentconcentrator.pcc.enumeration.ResultType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {
    private ResultType resultType;
    private Long issuerOrderId;
    private String issuerTimestamp;
}
