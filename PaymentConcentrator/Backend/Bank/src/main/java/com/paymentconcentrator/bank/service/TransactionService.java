package com.paymentconcentrator.bank.service;

import com.paymentconcentrator.bank.client.PaymentConcentratorClient;
import com.paymentconcentrator.bank.dto.*;
import com.paymentconcentrator.bank.enumeration.TransactionStatus;
import com.paymentconcentrator.bank.enumeration.TransactionType;
import com.paymentconcentrator.bank.model.Account;
import com.paymentconcentrator.bank.model.Card;
import com.paymentconcentrator.bank.model.Transaction;
import com.paymentconcentrator.bank.repository.AccountRepository;
import com.paymentconcentrator.bank.repository.CardRepository;
import com.paymentconcentrator.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final PaymentConcentratorClient paymentConcentratorClient;
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);


    public BankResponseDTO create(BankRequestDto dto) {
        BankResponseDTO response = new BankResponseDTO();
        Account acquirer = accountRepository.findByMerchantId(dto.getMerchantId());
        if (acquirer == null) {
            response.setUrl(dto.getErrorUrl());
            logger.error("Failed to create transaction. Cause: acquirer not found. DTO: " + dto.toString());
            return response;
        }
        //provera password-a (TODO: dodati hashovanje)
        if (!acquirer.getMerchantPassword().equals(dto.getMerchantPassword())) {
            response.setUrl(dto.getErrorUrl());
            logger.error("Failed to create transaction. Cause: Incorrect password for acquirer. DTO: " + dto.toString());
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
        response.setUrl("http://localhost:4400/purchase/"+paymentId);
        logger.info("Transaction created (id" + transaction.getId() +  ", paymentId: " + paymentId +"). DTO: " + dto.toString());
        return response;
    }

    public TransactionCompletedDTO checkIssuerData(IssuerDetailsDTO dto) {
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
                logger.info("Transaction cancelled. DTO: " + dto.toString());
                return new TransactionCompletedDTO(acquirerTransaction.getFailedUrl());
            }
            acquirerTransaction.setStatus(TransactionStatus.COMPLETED);
            transactionRepository.save(acquirerTransaction);
            Transaction issuerTransaction = new Transaction(acquirerTransaction.getAmount(), LocalDateTime.now(), TransactionType.OUTFLOW,
                    acquirerTransaction.getPaymentId(), TransactionStatus.COMPLETED, card.getAccount(), null,
                    null, null, acquirerTransaction.getMerchantOrderId());
            transactionRepository.save(issuerTransaction);
            moveFunds(acquirerTransaction.getAccount(), card.getAccount(), acquirerTransaction.getAmount());
            sendResultToPaymentConcentrator(acquirerTransaction);
            logger.info("Transaction completed. DTO: " + dto.toString());
            return new TransactionCompletedDTO(acquirerTransaction.getSuccessUrl());
        }
        return new TransactionCompletedDTO(acquirerTransaction.getFailedUrl());
    }

    private void moveFunds(Account acquirer, Account issuer, double amount) {
        acquirer.setFunds(acquirer.getFunds() + amount);
        issuer.setFunds(issuer.getFunds() - amount);
        accountRepository.save(acquirer);
        accountRepository.save(issuer);
    }

    private void sendResultToPaymentConcentrator(Transaction transaction) {
        paymentConcentratorClient.sendResult(new PcResultDTO(transaction.getMerchantOrderId(), transaction.getId(),
                transaction.getTimestamp().toString(), transaction.getPaymentId()));
    }
}
