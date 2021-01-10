package com.paymentconcentrator.bank.controller;

import com.paymentconcentrator.bank.dto.*;
import com.paymentconcentrator.bank.exception.NotFoundException;
import com.paymentconcentrator.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BankController {

	private final TransactionService transactionService;

	@PostMapping(value = "/pay")
	public ResponseEntity<String> createTransaction(@RequestBody BankRequestDto bankRequestDto){
		String response = transactionService.create(bankRequestDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/pay-part-2")
	public ResponseEntity<?> pay(@RequestBody IssuerDetailsDTO dto){
		try {
			TransactionCompletedDTO response = transactionService.checkIssuerData(dto);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (NotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/pay-pcc")
	public ResponseEntity<?> payPCC(@RequestBody PCCTransactionRequestDTO dto){
		try {
			PCCTransactionResponseDTO response = transactionService.checkIssuerDataPCC(dto);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
