package com.paymentconcentrator.bank.service;

import com.paymentconcentrator.bank.client.PCCClient;
import com.paymentconcentrator.bank.dto.*;
import com.paymentconcentrator.bank.exception.NotFoundException;
import com.paymentconcentrator.bank.mapper.AccountMapper;
import com.paymentconcentrator.bank.model.Account;
import com.paymentconcentrator.bank.model.Card;
import com.paymentconcentrator.bank.repository.AccountRepository;
import com.paymentconcentrator.bank.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final PCCClient pccClient;
    private final AccountMapper accountMapper = new AccountMapper();

    @Value("${bank.bin}")
    private String bin;

    @Value("${server.port}")
    private String port;

    @Transactional(rollbackFor=Exception.class)
    public AccountDTO create(AccountCreateDTO dto) {
        Account account = new Account();

        account.setFirstName(dto.getName());
        account.setLastName(dto.getSurname());
        account.setFunds(0.0);
        account.setCards(new ArrayList<>());
        account.setTransactions(new ArrayList<>());
        account = accountRepository.save(account);
        Card card = new Card();
        card.setCardHolderName(dto.getName() + " " + dto.getSurname());
        String expDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy"));
        card.setExpDate(expDate);
        do {
            card.setNumber(bin + generateRandomNumbers(10));
        } while (cardRepository.findByNumber(card.getNumber()) != null); //ponavljaj dok ne dobijes random broj koji ne postoji vec
        card.setSecurityCode(generateRandomNumbers(3));
        card.setAccount(account);

        cardRepository.save(card);
        pccClient.createAccount(new PCCAccountCreateDTO(card.getNumber(), "http://localhost:"+port));
        return accountMapper.toDtoWithCard(card.getAccount(), card);
    }

    public String generateRandomNumbers(int targetStringLength) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 57; // numeral '9'
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    public AccountDTO get(Long id) throws Exception {
        Account account = accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Account not found"));
        return accountMapper.toDto(account);
    }

    public AccountDTO addFunds(AccountAddFundsDTO dto) throws Exception {
        Account account = accountRepository.findById(dto.getId()).orElseThrow(() -> new NotFoundException("Account not found"));
        account.setFunds(account.getFunds() + dto.getAmount());
        accountRepository.save(account);
        return accountMapper.toDto(account);
    }
}
