package com.paymentconcentrator.bank.service;

import com.paymentconcentrator.bank.dto.BankRequestDto;
import com.paymentconcentrator.bank.dto.BankResponseDTO;
import com.paymentconcentrator.bank.dto.IssuerDetailsDTO;
import com.paymentconcentrator.bank.enumeration.TransactionStatus;
import com.paymentconcentrator.bank.enumeration.TransactionType;
import com.paymentconcentrator.bank.model.Account;
import com.paymentconcentrator.bank.model.Card;
import com.paymentconcentrator.bank.model.Transaction;
import com.paymentconcentrator.bank.repository.AccountRepository;
import com.paymentconcentrator.bank.repository.CardRepository;
import com.paymentconcentrator.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    public BankResponseDTO create(BankRequestDto dto) {
        BankResponseDTO response = new BankResponseDTO();
        Account acquirer = accountRepository.findByMerchantId(dto.getMerchantId());
        if (acquirer == null) {
            response.setUrl(dto.getErrorUrl());
            return response;
        }
        //provera password-a (TODO: dodati hashovanje)
        if (!acquirer.getMerchantPassword().equals(dto.getMerchantPassword())) {
            response.setUrl(dto.getErrorUrl());
            return response;
        }
        Long paymentId = 0L;
        Transaction maxPaymentIdTransaction = transactionRepository.findFirstByTypeOrderByPaymentIdDesc(TransactionType.INFLOW);
        if (maxPaymentIdTransaction != null) {
            paymentId = maxPaymentIdTransaction.getPaymentId() + 1;
        }
        Transaction transaction = new Transaction(dto.getAmount(), LocalDateTime.now(), TransactionType.INFLOW, paymentId,
                TransactionStatus.IN_PROGRESS, acquirer, dto.getSuccessUrl(), dto.getFailedUrl(), dto.getErrorUrl(), dto.getMerchantOrderId());
        transactionRepository.save(transaction);
        response.setId(paymentId);
        response.setUrl("localhost:");
        return response;
    }

    public String checkIssuerData(IssuerDetailsDTO dto) {
        Transaction acquirerTransaction = transactionRepository.findFirstByPaymentIdOrderByType(dto.getPaymentId());
        Card card = cardRepository.findByNumber(dto.getNumber());
        if (card == null) {
            //TODO: Poslati PCC-u
        } else {
            if (!dto.getSecurityCode().equals(card.getSecurityCode()) || !dto.getExpDate().equals(card.getExpDate()) ||
                    !dto.getCardHolderName().equals(card.getCardHolderName()) ||
                    card.getAccount().getFunds() < acquirerTransaction.getAmount()) {
                acquirerTransaction.setStatus(TransactionStatus.CANCELLED);
                transactionRepository.save(acquirerTransaction);
                return acquirerTransaction.getFailedUrl();
            }
            acquirerTransaction.setStatus(TransactionStatus.COMPLETED);
            transactionRepository.save(acquirerTransaction);
            Transaction transaction = new Transaction(acquirerTransaction.getAmount(), LocalDateTime.now(), TransactionType.OUTFLOW,
                    acquirerTransaction.getPaymentId(), TransactionStatus.COMPLETED, card.getAccount(), null,
                    null, null, acquirerTransaction.getMerchantOrderId());
            transactionRepository.save(transaction);
            moveFunds(acquirerTransaction.getAccount(), card.getAccount(), acquirerTransaction.getAmount());
            return acquirerTransaction.getSuccessUrl();
        }
        return acquirerTransaction.getFailedUrl();
    }

    private void moveFunds(Account acquirer, Account issuer, double amount) {
        acquirer.setFunds(acquirer.getFunds() + amount);
        issuer.setFunds(issuer.getFunds() - amount);
        accountRepository.save(acquirer);
        accountRepository.save(issuer);
    }
}
