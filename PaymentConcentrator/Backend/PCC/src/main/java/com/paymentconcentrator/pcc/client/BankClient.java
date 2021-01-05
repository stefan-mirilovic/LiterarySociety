package com.paymentconcentrator.pcc.client;

import com.paymentconcentrator.pcc.dto.TransactionRequestDTO;
import com.paymentconcentrator.pcc.dto.TransactionResponseDTO;
import feign.Headers;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestMapping;

public interface BankClient {

    @RequestLine("POST")
    @RequestMapping
    @Headers("Content-Type: application/json")
    TransactionResponseDTO forwardToBank(TransactionRequestDTO dto);
}
