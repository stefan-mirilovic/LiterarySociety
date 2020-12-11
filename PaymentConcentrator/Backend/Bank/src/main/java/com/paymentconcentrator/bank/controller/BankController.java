package com.paymentconcentrator.bank.controller;

import com.paymentconcentrator.bank.dto.BankRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BankController {

	@PostMapping
	public void getBank(@RequestBody BankRequestDto bankRequestDto){
		System.out.println("Hello from bank");
	}
}
