package com.paymentconcentrator.bank.controller;

import com.paymentconcentrator.bank.dto.*;
import com.paymentconcentrator.bank.exception.NotFoundException;
import com.paymentconcentrator.bank.service.AccountService;
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

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id){
        try {
            AccountDTO response = accountService.get(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AccountCreateDTO dto){
        try {
            AccountDTO response = accountService.create(dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/add-funds")
    public ResponseEntity<?> addFunds(@RequestBody AccountAddFundsDTO dto){
        try {
            AccountDTO response = accountService.addFunds(dto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
