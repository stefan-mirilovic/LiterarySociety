package com.paymentconcentrator.cp.controller;

import com.paymentconcentrator.cp.dto.OrderDto;
import com.paymentconcentrator.cp.dto.PaymentDto;
import com.paymentconcentrator.cp.service.DiscoveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChosePaymentController {

	private final DiscoveryService discoveryService;

	@GetMapping("/chose/payment")
	public ResponseEntity<OrderDto> chosePayment(OrderDto orderDto){
		orderDto.setMerchantId("e655cc16-7442-40eb-98e1-abf9690c4152");
		orderDto.setAmount(3142.7);
		return new ResponseEntity<>(orderDto, HttpStatus.OK);
	}

	@GetMapping("/get/all/payments")
	public ResponseEntity<?> getAllPayments(){
		return new ResponseEntity<>(discoveryService.discoveryPayments(),HttpStatus.OK);
	}
}
