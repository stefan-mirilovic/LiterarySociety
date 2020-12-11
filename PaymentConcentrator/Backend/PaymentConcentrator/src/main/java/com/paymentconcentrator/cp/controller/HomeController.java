package com.paymentconcentrator.cp.controller;

import com.netflix.ribbon.proxy.annotation.Http;
import com.paymentconcentrator.cp.client.BankClient;
import com.paymentconcentrator.cp.dto.BankRequestDto;
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

    private final BankClient bankClient;
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
        orderDto.setMerchantOrderId("2b7d5124-5688-47e8-800e-412d0e965302");
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @PostMapping("/pay/bank")
    public void pay(@RequestBody OrderDto orderDto){
        BankRequestDto bankRequestDto = requestBankService.createRequest(orderDto);
        bankClient.getBank(bankRequestDto);
    }



}

