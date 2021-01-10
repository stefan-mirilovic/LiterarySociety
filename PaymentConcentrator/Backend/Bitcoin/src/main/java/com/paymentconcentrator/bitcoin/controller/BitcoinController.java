package com.paymentconcentrator.bitcoin.controller;

import com.paymentconcentrator.bitcoin.client.PaymentConcentratorClient;
import com.paymentconcentrator.bitcoin.service.PaymentService;
import com.paymentconcentrator.bitcoin.utils.dto.*;
import com.paymentconcentrator.bitcoin.utils.globals.PaymentConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/api/pay")
@CrossOrigin(origins = "http://localhost:4200")
public class BitcoinController {

	private final static String GET_SUCCESS = "/paymentSuccessful/{id}";

	private final PaymentService paymentService;
	private final PaymentConcentratorClient paymentConcentratorClient;

	@PostMapping
	public String sendOrder(@RequestBody ConcentratorRequest concentratorRequest){
		return paymentService.sendOrder(concentratorRequest);
	}

	@GetMapping(GET_SUCCESS)
	public String getPaymentSuccess(@PathVariable("id") Long paymentId){
		BitcoinResultDto bitcoinResultDto = new BitcoinResultDto();
		bitcoinResultDto.setMerchantOrderId(paymentId);
		bitcoinResultDto.setPaymentMethod(PaymentConstants.Info.PAYMENT_METHOD);
		paymentConcentratorClient.sendResult(bitcoinResultDto);
		return PaymentConstants.Url.REDIRECT_SUCCESS;
	}

	@PostMapping(value = "/merchant-connect")
	public ResponseEntity<?> connectMerchant(@RequestBody MerchantConnectRequestDTO dto){
		try {
			MerchantConnectRequestDTO response = paymentService.connectMerchant(dto);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}


}
