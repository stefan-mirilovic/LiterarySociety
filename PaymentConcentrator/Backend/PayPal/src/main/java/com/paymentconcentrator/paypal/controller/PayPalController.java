package com.paymentconcentrator.paypal.controller;

import com.paymentconcentrator.paypal.client.PaymentConcentratorClient;
import com.paymentconcentrator.paypal.dto.MerchantConnectRequestDTO;
import com.paymentconcentrator.paypal.dto.PayPalRequestDto;
import com.paymentconcentrator.paypal.dto.PayPalResultDto;
import com.paymentconcentrator.paypal.service.PayPalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/pay")
public class PayPalController {
	private final PaymentConcentratorClient paymentConcentratorClient;
	private final PayPalService payPalService;
	public static final String SUCCESS_URL = "/success";
	public static final String CANCEL_URL = "/cancel";


	@PostMapping()
	public String pay(@RequestBody PayPalRequestDto payPalRequestDto) throws PayPalRESTException, IOException {
		Payment payment = payPalService.redirectUrl(payPalRequestDto);
		String response = payPalService.redirectLink(payment);
		return response;
	}

	@GetMapping(CANCEL_URL)
	public String cancelPay() {
		return "cancel";
	}

	@GetMapping(SUCCESS_URL+"/{id}")
	public RedirectView successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, @PathVariable(value = "id") Long merchantOrder) throws PayPalRESTException {
		PayPalResultDto payment = payPalService.executePayment(paymentId, payerId, merchantOrder);
		paymentConcentratorClient.sendResult(payment);
		return new RedirectView("https://screenmessage.com/hxqx");
	}

	@PostMapping(value = "/merchant-connect")
	public ResponseEntity<?> connectMerchant(@RequestBody MerchantConnectRequestDTO dto){
		try {
			MerchantConnectRequestDTO response = payPalService.connectMerchant(dto);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
