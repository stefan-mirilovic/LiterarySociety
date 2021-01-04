package com.paymentconcentrator.bank.client;

import com.paymentconcentrator.bank.dto.PCCAccountCreateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "pcc")
public interface PCCClient {

    @PostMapping(value = "/api/accounts")
    void createAccount(PCCAccountCreateDTO dto);
}
