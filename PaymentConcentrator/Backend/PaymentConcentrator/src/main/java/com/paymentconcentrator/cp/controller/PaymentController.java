package com.paymentconcentrator.cp.controller;

import com.netflix.ribbon.proxy.annotation.Http;
import com.paymentconcentrator.cp.client.PaymentClient;
import com.paymentconcentrator.cp.dto.OrderDto;
import com.paymentconcentrator.cp.dto.PayPalResponseDto;
import com.paymentconcentrator.cp.dto.RedirectDto;
import com.paymentconcentrator.cp.service.DiscoveryService;
import com.paymentconcentrator.cp.service.RequestPaymentService;
import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.netflix.ribbon.okhttp.OkHttpLoadBalancingClient;
import org.springframework.cloud.netflix.ribbon.okhttp.OkHttpRibbonRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;

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
	public void receiveResult(@RequestBody PayPalResponseDto payPalResponseDto){
		requestPaymentService.receiveResult(payPalResponseDto);
	}
}
