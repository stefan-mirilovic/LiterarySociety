package com.paymentconcentrator.pcc.service;

import com.paymentconcentrator.pcc.client.BankClient;
import com.paymentconcentrator.pcc.dto.TransactionRequestDTO;
import com.paymentconcentrator.pcc.dto.TransactionResponseDTO;
import com.paymentconcentrator.pcc.enumeration.ResultType;
import com.paymentconcentrator.pcc.model.Account;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountService accountService;

    public TransactionResponseDTO create(TransactionRequestDTO dto) {
        Account account = accountService.findByCardNumber(dto.getNumber());
        if (account == null) {
            return new TransactionResponseDTO(ResultType.INVALID_CREDENTIALS, null, null);
        }
        BankClient bankClient = Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(BankClient.class, account.getBankUrl() + "/api/pay-pcc");
        TransactionResponseDTO response = bankClient.forwardToBank(dto);
        return response;
    }
}
