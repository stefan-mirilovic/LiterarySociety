package com.paymentconcentrator.pcc.controller;

import com.paymentconcentrator.pcc.dto.AccountCreateDTO;
import com.paymentconcentrator.pcc.dto.AccountCreateResponseDTO;
import com.paymentconcentrator.pcc.exception.NotFoundException;
import com.paymentconcentrator.pcc.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountCreateDTO dto){
        try {
            AccountCreateResponseDTO response = accountService.create(dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
