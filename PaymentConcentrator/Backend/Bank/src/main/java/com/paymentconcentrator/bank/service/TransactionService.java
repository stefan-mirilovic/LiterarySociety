package com.paymentconcentrator.bank.service;

import com.paymentconcentrator.bank.client.PCCClient;
import com.paymentconcentrator.bank.client.PaymentConcentratorClient;
import com.paymentconcentrator.bank.dto.*;
import com.paymentconcentrator.bank.enumeration.PCCResultType;
import com.paymentconcentrator.bank.enumeration.TransactionStatus;
import com.paymentconcentrator.bank.enumeration.TransactionType;
import com.paymentconcentrator.bank.exception.InvalidCredentialsException;
import com.paymentconcentrator.bank.exception.NotFoundException;
import com.paymentconcentrator.bank.model.Account;
import com.paymentconcentrator.bank.model.Card;
import com.paymentconcentrator.bank.model.Transaction;
import com.paymentconcentrator.bank.repository.AccountRepository;
import com.paymentconcentrator.bank.repository.CardRepository;
import com.paymentconcentrator.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final PaymentConcentratorClient paymentConcentratorClient;
    private final PCCClient pccClient;
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Value("${bank.frontend}")
    private String frontendUrl;

    @Scheduled(fixedRate = 200000)
    public void autoFeed() {
        logger.info("Transaction check starting...");
        List<Transaction> transactions = transactionRepository.findByStatus(TransactionStatus.IN_PROGRESS);
        for (Transaction t: transactions) {
            if (t.getTimestamp().isBefore(LocalDateTime.now().minusMinutes(20))) {
                t.setStatus(TransactionStatus.CANCELLED);
                transactionRepository.save(t);
                logger.info("Transaction cancelled for taking too long to complete. ID: "+t.getId());
            }
        }
        logger.info("Transaction check finished");
    }

    public String create(BankRequestDto dto) {
        BankResponseDTO response = new BankResponseDTO();
        try {
            Account acquirer = accountRepository.findByMerchantId(dto.getMerchantId());
            if (acquirer == null) {
                response.setRedirectLink(dto.getErrorUrl());
                logger.error("Failed to create transaction. Cause: acquirer not found. DTO: " + dto.toString());
                return dto.getErrorUrl();
            }
            //provera password-a (TODO: dodati hashovanje)
            if (!acquirer.getMerchantPassword().equals(dto.getMerchantPassword())) {
                response.setRedirectLink(dto.getErrorUrl());
                logger.error("Failed to create transaction. Cause: Incorrect password for acquirer. DTO: " + dto.toString());
                return dto.getErrorUrl();
            }
            Long paymentId = 0L;
            Transaction maxPaymentIdTransaction = transactionRepository.findFirstByTypeOrderByPaymentIdDesc(TransactionType.INFLOW);
            if (maxPaymentIdTransaction != null) {
                paymentId = maxPaymentIdTransaction.getPaymentId() + 1;
            }
            Transaction transaction = new Transaction(dto.getAmount(), LocalDateTime.now(), TransactionType.INFLOW, paymentId,
                    TransactionStatus.IN_PROGRESS, acquirer, dto.getSuccessUrl(), dto.getFailedUrl(), dto.getErrorUrl(), dto.getMerchantOrderId());
            transactionRepository.save(transaction);
            response.setRedirectLink(frontendUrl + "/purchase/" + paymentId);
            logger.info("Transaction created (id" + transaction.getId() + ", paymentId: " + paymentId + "). DTO: " + dto.toString());
            return frontendUrl + "/purchase/" + paymentId;
        } catch (Exception e) {
            response.setRedirectLink(dto.getErrorUrl());
            logger.error("Exception caught while creating transaction: " + e.getMessage());
            return dto.getErrorUrl();
        }
    }

    public TransactionCompletedDTO checkIssuerData(IssuerDetailsDTO dto) throws Exception {
        Transaction acquirerTransaction = transactionRepository.findFirstByPaymentIdOrderByType(dto.getPaymentId());
        try {
            if (acquirerTransaction == null) {
                throw new NotFoundException("Transaction does not exist!");
            }
            Card card = cardRepository.findByNumber(dto.getNumber());
            if (card == null) {
                //Preko PCC-a
                PCCTransactionRequestDTO request = new PCCTransactionRequestDTO(dto.getNumber(), dto.getSecurityCode(),
                        dto.getCardHolderName(), dto.getExpDate(), acquirerTransaction.getId(), acquirerTransaction.getAmount(),
                        acquirerTransaction.getMerchantOrderId());
                logger.info("Transaction forwarded to PCC. AcquirerOrderId: " + acquirerTransaction.getId());
                PCCTransactionResponseDTO response = pccClient.forwardToPCC(request);
                switch (response.getResultType()) {
                    case COMPLETED:
                        addFunds(acquirerTransaction.getAccount(), acquirerTransaction.getAmount());
                        acquirerTransaction.setStatus(TransactionStatus.COMPLETED);
                        acquirerTransaction.setForeignOrderId(response.getIssuerOrderId());
                        transactionRepository.save(acquirerTransaction);
                        logger.info("Transaction (acquirer) completed via PCC. AcquirerOrderId: " + acquirerTransaction.getId() + " IssuerOrderId: " + response.getIssuerOrderId());
                        return new TransactionCompletedDTO(acquirerTransaction.getSuccessUrl());
                    case INVALID_CREDENTIALS:
                        //logger.info("Transaction (acquirer) attempted with invalid credentials via PCC. ID: "+acquirerTransaction.getId()+" PaymentID: "+acquirerTransaction.getPaymentId());
                        throw new InvalidCredentialsException("Invalid Credentials!");
                    case INSUFFICIENT_FUNDS:
                        acquirerTransaction.setStatus(TransactionStatus.CANCELLED);
                        transactionRepository.save(acquirerTransaction);
                        sendResultToPaymentConcentrator(acquirerTransaction);
                        logger.info("Transaction (acquirer) cancelled. AcquirerOrderId: " + acquirerTransaction.getId() + " IssuerOrderId: " + response.getIssuerOrderId());
                        return new TransactionCompletedDTO(acquirerTransaction.getFailedUrl());

                }
            } else {
                //Unutar iste banke
                if (!dto.getSecurityCode().equals(card.getSecurityCode()) || !dto.getExpDate().equals(card.getExpDate()) ||
                        !dto.getCardHolderName().equals(card.getCardHolderName())) {
                    logger.info("Transaction attempted with invalid credentials. ID: " + acquirerTransaction.getId() + " PaymentID: " + acquirerTransaction.getPaymentId());
                    throw new InvalidCredentialsException("Invalid Credentials!");
                }
                if (card.getAccount().getFunds() < acquirerTransaction.getAmount()) {
                    acquirerTransaction.setStatus(TransactionStatus.CANCELLED);
                    transactionRepository.save(acquirerTransaction);
                    logger.info("Transaction cancelled. ID: " + acquirerTransaction.getId() + " PaymentID: " + acquirerTransaction.getPaymentId());
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
                logger.info("Transaction completed. ID: " + acquirerTransaction.getId() + " PaymentID: " + acquirerTransaction.getPaymentId());
                return new TransactionCompletedDTO(acquirerTransaction.getSuccessUrl());
            }
            return new TransactionCompletedDTO(acquirerTransaction.getFailedUrl());
        } catch (InvalidCredentialsException e) {
            throw new InvalidCredentialsException(e.getMessage());
        }catch (Exception e) {
            logger.error("Exception caught while processing transaction: " + e.getMessage());
            return new TransactionCompletedDTO(acquirerTransaction.getErrorUrl());
        }
    }

    private void moveFunds(Account acquirer, Account issuer, double amount) {
        acquirer.setFunds(acquirer.getFunds() + amount);
        issuer.setFunds(issuer.getFunds() - amount);
        accountRepository.save(acquirer);
        accountRepository.save(issuer);
    }

    private void removeFunds(Account issuer, double amount) {
        issuer.setFunds(issuer.getFunds() - amount);
        accountRepository.save(issuer);
    }

    private void addFunds(Account acquirer, double amount) {
        acquirer.setFunds(acquirer.getFunds() + amount);
        accountRepository.save(acquirer);
    }

    private void sendResultToPaymentConcentrator(Transaction transaction) {
        paymentConcentratorClient.sendResult(new PcResultDTO(transaction.getMerchantOrderId(), transaction.getId(),
                transaction.getTimestamp().toString(), transaction.getPaymentId()));
    }

    public PCCTransactionResponseDTO checkIssuerDataPCC(PCCTransactionRequestDTO dto) {
        Card card = cardRepository.findByNumber(dto.getNumber());
        if (!dto.getSecurityCode().equals(card.getSecurityCode()) || !dto.getExpDate().equals(card.getExpDate()) ||
                !dto.getCardHolderName().equals(card.getCardHolderName())) {
            logger.info("Transaction (issuer) attempted with invalid credentials via PCC. AccountID: "+card.getAccount().getId()+" Acquirer Order ID: "+dto.getAcquirerOrderId());
            return new PCCTransactionResponseDTO(PCCResultType.INVALID_CREDENTIALS, null, null);
        }
        if (card.getAccount().getFunds() < dto.getAmount()) {
            logger.info("Transaction (issuer) attempted with insufficient funds via PCC. AccountID: "+card.getAccount().getId()+" Acquirer Order ID: "+dto.getAcquirerOrderId());
            return new PCCTransactionResponseDTO(PCCResultType.INSUFFICIENT_FUNDS, null, null);
        }
        Transaction issuerTransaction = new Transaction(dto.getAmount(), LocalDateTime.now(), TransactionType.OUTFLOW,
                null, TransactionStatus.COMPLETED, card.getAccount(), null,
                null, null, dto.getMerchantOrderId());
        issuerTransaction.setForeignOrderId(dto.getAcquirerOrderId());
        transactionRepository.save(issuerTransaction);
        removeFunds(card.getAccount(), dto.getAmount());
        logger.info("Transaction (issuer) completed. IssuerOrderID: "+issuerTransaction.getId()+" Acquirer Order ID: "+dto.getAcquirerOrderId());
        return new PCCTransactionResponseDTO(PCCResultType.COMPLETED, issuerTransaction.getId(),
                issuerTransaction.getTimestamp().toString());
    }
}
