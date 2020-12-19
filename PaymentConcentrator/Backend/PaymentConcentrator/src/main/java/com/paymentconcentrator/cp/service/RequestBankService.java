package com.paymentconcentrator.cp.service;

import com.paymentconcentrator.cp.dto.BankRequestDto;
import com.paymentconcentrator.cp.dto.BankResponseDTO;
import com.paymentconcentrator.cp.dto.BankResultDTO;
import com.paymentconcentrator.cp.dto.OrderDto;
import org.springframework.stereotype.Service;

public interface RequestBankService {

	/**
	 * Method for creating bankRequest
	 * @param orderDto contains price and id of order
	 * @return BankRequestDto
	 */
	BankResponseDTO createRequest(OrderDto orderDto);

    void receiveResult(BankResultDTO dto);
}
