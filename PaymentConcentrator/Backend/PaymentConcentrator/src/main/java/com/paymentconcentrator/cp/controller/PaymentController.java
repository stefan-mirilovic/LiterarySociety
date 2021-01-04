package com.paymentconcentrator.cp.controller;

import com.paymentconcentrator.cp.client.GenericPaymentClient;
import com.paymentconcentrator.cp.dto.OrderDto;
import com.paymentconcentrator.cp.dto.GenericPaymentResponseDto;
import com.paymentconcentrator.cp.service.DiscoveryService;
import com.paymentconcentrator.cp.service.RequestPaymentService;
import feign.Feign;
import feign.gson.GsonEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/payment/type")
@RequiredArgsConstructor
@Import(FeignClientsConfiguration.class)
public class PaymentController {

	private final RequestPaymentService requestPaymentService;
	private final DiscoveryService discoveryService;

	@PostMapping("/pay")
	public ResponseEntity<?> pay(@RequestBody OrderDto orderDto) {
		return new ResponseEntity<>(requestPaymentService.createRequest(orderDto),HttpStatus.OK);
	}

	@PostMapping("/result")
	public void receiveResult(@RequestBody GenericPaymentResponseDto genericPaymentResponseDto){
		requestPaymentService.receiveResult(genericPaymentResponseDto);
	}

	@PostMapping("/custom/pay")
	public void customPay(){
		GenericPaymentClient genericPaymentClient = Feign.builder()
				.encoder(new GsonEncoder())
				.target(GenericPaymentClient.class, "http://localhost:8084/api/pay");
//		TestDto testDto = new TestDto();
//		testDto.setName("Hello");
//		genericPaymentClient.test(testDto);
	}
}
