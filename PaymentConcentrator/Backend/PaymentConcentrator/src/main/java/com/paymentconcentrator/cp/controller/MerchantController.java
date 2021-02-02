package com.paymentconcentrator.cp.controller;

import com.paymentconcentrator.cp.dto.MerchantBankConnectDTO;
import com.paymentconcentrator.cp.dto.MerchantConnectDTO;
import com.paymentconcentrator.cp.dto.MerchantDTO;
import com.paymentconcentrator.cp.dto.PaymentDto;
import com.paymentconcentrator.cp.service.impl.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/merchants")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MerchantDTO dto){
        try {
            MerchantDTO response = merchantService.create(dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MerchantDTO dto){
        try {
            MerchantDTO response = merchantService.login(dto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}/payment-types")
    public ResponseEntity<?> getPaymentTypes(@PathVariable String id){
        try {
            List<PaymentDto> response = merchantService.getPaymentTypes(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/{id}/payment-types")
    public ResponseEntity<?> addPaymentType(@PathVariable String id, @RequestBody MerchantConnectDTO dto){
        try {
            PaymentDto response = merchantService.addPaymentType(id, dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/{id}/payment-types/bank")
    public ResponseEntity<?> addPaymentTypeBank(@PathVariable String id, @RequestBody MerchantBankConnectDTO dto){
        try {
            PaymentDto response = merchantService.addPaymentTypeBank(id, dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{merchantId}/payment-types/{id}")
    public ResponseEntity<?> deletePaymentType(@PathVariable String merchantId, @PathVariable Long id){
        try {
            PaymentDto response = merchantService.deletePaymentTypeBank(merchantId, id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
