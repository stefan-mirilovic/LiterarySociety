package com.paymentconcentrator.bitcoin.utils;


import com.paymentconcentrator.bitcoin.utils.dto.ApiResponseDto;
import com.paymentconcentrator.bitcoin.utils.dto.PreparedPaymentDto;
import com.paymentconcentrator.bitcoin.utils.globals.PaymentConstants;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.paymentconcentrator.bitcoin.utils.globals.PaymentConstants.Api.API_ORDERS;
import static com.paymentconcentrator.bitcoin.utils.globals.PaymentConstants.Info.*;
import static com.paymentconcentrator.bitcoin.utils.globals.PaymentConstants.Header.*;
import static com.paymentconcentrator.bitcoin.utils.globals.PaymentConstants.BodyParam.*;

public final class PaymentUtils {

	private PaymentUtils() {

	}

	public static ResponseEntity<ApiResponseDto> postOrder(PreparedPaymentDto preparedPaymentDto) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.add(AUTHORIZATION, TOKEN + preparedPaymentDto.getApiToken());

		MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
		bodyParams.add(TITLE, preparedPaymentDto.getTitle());
		bodyParams.add(CANCEL_URL, preparedPaymentDto.getCancelUrl());
		bodyParams.add(SUCCESS_URL, preparedPaymentDto.getSuccessUrl());
		bodyParams.add(PRICE_CURRENCY, preparedPaymentDto.getCurrency());
		bodyParams.add(RECEIVE_CURRENCY, preparedPaymentDto.getCurrency());
		bodyParams.add(PRICE_AMOUNT, preparedPaymentDto.getAmount().toString());

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(bodyParams, headers);
		return restTemplate.postForEntity(API_ORDERS,request, ApiResponseDto.class);
	}

}
