package com.paymentconcentrator.pcc.controller;

import com.paymentconcentrator.pcc.dto.AccountCreateDTO;
import com.paymentconcentrator.pcc.dto.AccountCreateResponseDTO;
import com.paymentconcentrator.pcc.dto.TransactionRequestDTO;
import com.paymentconcentrator.pcc.dto.TransactionResponseDTO;
import com.paymentconcentrator.pcc.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequestDTO dto){
        try {
            TransactionResponseDTO response = transactionService.create(dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
