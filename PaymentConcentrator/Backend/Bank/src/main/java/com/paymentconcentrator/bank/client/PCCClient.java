package com.paymentconcentrator.bank.client;

import com.paymentconcentrator.bank.dto.PCCAccountCreateDTO;
import com.paymentconcentrator.bank.dto.PCCTransactionRequestDTO;
import com.paymentconcentrator.bank.dto.PCCTransactionResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "pcc")
public interface PCCClient {

    @PostMapping(value = "/api/accounts")
    void createAccount(PCCAccountCreateDTO dto);

    @PostMapping(value = "/api/transactions")
    PCCTransactionResponseDTO forwardToPCC(PCCTransactionRequestDTO dto);
}
