package com.paymentconcentrator.cp.controller;

import com.paymentconcentrator.cp.dto.BankResponseDTO;
import com.paymentconcentrator.cp.dto.BankResultDTO;
import com.paymentconcentrator.cp.dto.OrderDto;
import com.paymentconcentrator.cp.service.RequestBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeController {

    private final RequestBankService requestBankService;

    @Value("${server.port}")
    private String port;

    @GetMapping
    public String home(){
        return "hello from payment concentrator : " + port;
    }

    @GetMapping("/chose/payment")
    public ResponseEntity<OrderDto> chosePayment(OrderDto orderDto){
        orderDto.setMerchantId("e655cc16-7442-40eb-98e1-abf9690c4152");
        orderDto.setAmount(3142.7);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @PostMapping("/pay/bank")
    public ResponseEntity<BankResponseDTO> pay(@RequestBody OrderDto orderDto){
        return new ResponseEntity<>(requestBankService.createRequest(orderDto), HttpStatus.OK);
    }

    @PostMapping("result/bank")
    public void receiveResult(@RequestBody BankResultDTO dto) {
        requestBankService.receiveResult(dto);
    }

}

