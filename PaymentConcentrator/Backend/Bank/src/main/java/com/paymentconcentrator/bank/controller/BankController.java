package com.paymentconcentrator.bank.controller;

import com.paymentconcentrator.bank.dto.BankRequestDto;
import com.paymentconcentrator.bank.dto.BankResponseDTO;
import com.paymentconcentrator.bank.dto.IssuerDetailsDTO;
import com.paymentconcentrator.bank.dto.TransactionCompletedDTO;
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

	@PostMapping
	public ResponseEntity<BankResponseDTO> getBank(@RequestBody BankRequestDto bankRequestDto){
		BankResponseDTO response = transactionService.create(bankRequestDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/pay")
	public ResponseEntity<?> pay(@RequestBody IssuerDetailsDTO dto){
		TransactionCompletedDTO response = transactionService.checkIssuerData(dto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
