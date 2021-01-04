package com.paymentconcentrator.pcc.service;

import com.paymentconcentrator.pcc.dto.AccountCreateDTO;
import com.paymentconcentrator.pcc.dto.AccountCreateResponseDTO;
import com.paymentconcentrator.pcc.model.Account;
import com.paymentconcentrator.pcc.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional(rollbackFor=Exception.class)
    public AccountCreateResponseDTO create(AccountCreateDTO dto) {
        Account account = new Account(null, dto.getCardNumber(), dto.getBankUrl());
        accountRepository.save(account);
        return new AccountCreateResponseDTO(true);
    }
}
