package com.paymentconcentrator.bitcoin.controller;

import com.paymentconcentrator.bitcoin.client.PaymentConcentratorClient;
import com.paymentconcentrator.bitcoin.service.PaymentService;
import com.paymentconcentrator.bitcoin.utils.dto.BitcoinResultDto;
import com.paymentconcentrator.bitcoin.utils.dto.ConcentratorRequest;
import com.paymentconcentrator.bitcoin.utils.dto.PaymentUrlDto;
import com.paymentconcentrator.bitcoin.utils.dto.TestDto;
import com.paymentconcentrator.bitcoin.utils.globals.PaymentConstants;
import lombok.RequiredArgsConstructor;
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




}
