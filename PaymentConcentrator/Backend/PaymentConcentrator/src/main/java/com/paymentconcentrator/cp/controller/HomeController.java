package com.paymentconcentrator.cp.controller;

import com.paymentconcentrator.cp.client.BankClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeController {

    private final BankClient bankClient;

    @GetMapping
    public String home(){
        return "hello from payment concentrator";
    }

    @GetMapping("/bank")
    public String getBank(){
        return bankClient.getTest();
    }



}

