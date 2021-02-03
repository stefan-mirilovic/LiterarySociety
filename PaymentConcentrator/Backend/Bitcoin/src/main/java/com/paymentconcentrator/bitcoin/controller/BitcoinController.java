package com.paymentconcentrator.bitcoin.controller;

import com.paymentconcentrator.bitcoin.client.PaymentConcentratorClient;
import com.paymentconcentrator.bitcoin.service.PaymentService;
import com.paymentconcentrator.bitcoin.utils.dto.*;
import com.paymentconcentrator.bitcoin.utils.globals.PaymentConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@CrossOrigin
@RestController()
@RequiredArgsConstructor
@RequestMapping("/api/pay")
public class BitcoinController {

	private final static String GET_SUCCESS = "/paymentSuccessful/{id}";

	private final PaymentService paymentService;
	private final PaymentConcentratorClient paymentConcentratorClient;

	@PostMapping
	public String sendOrder(@RequestBody ConcentratorRequest concentratorRequest){
		return paymentService.sendOrder(concentratorRequest);
	}

	@GetMapping("/"+PaymentConstants.Url.SUCCESS_URL+"{id}")
	public RedirectView getPaymentSuccess(@PathVariable("id") Long paymentId){
		String successUrl = paymentService.finishTransaction(paymentId);
		return new RedirectView(successUrl);
	}

	@GetMapping("/"+PaymentConstants.Url.CANCEL_URL+"{id}")
	public RedirectView getPaymentCancel(@PathVariable("id") Long paymentId){
		String url = paymentService.cancelTransaction(paymentId);
		return new RedirectView(url);
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
