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

@CrossOrigin
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BankController {

    private final RequestBankService requestBankService;

    @Value("${server.port}")
    private String port;

    @GetMapping
    public String home(){
        return "hello from payment concentrator : " + port;
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

