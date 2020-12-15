package com.paymentconcentrator.bank.client;

import com.paymentconcentrator.bank.dto.PcResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "payment-concentrator")
public interface PaymentConcentratorClient {

    @PostMapping(value = "/api/result/bank")
    public void sendResult(PcResultDTO bankRequestDto);
}
