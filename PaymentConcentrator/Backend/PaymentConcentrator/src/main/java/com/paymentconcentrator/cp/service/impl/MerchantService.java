package com.paymentconcentrator.cp.service.impl;

import com.paymentconcentrator.cp.client.BankClient;
import com.paymentconcentrator.cp.dto.*;
import com.paymentconcentrator.cp.mapper.MerchantMapper;
import com.paymentconcentrator.cp.mapper.PaymentTypeMapper;
import com.paymentconcentrator.cp.model.Merchant;
import com.paymentconcentrator.cp.model.PaymentType;
import com.paymentconcentrator.cp.repository.MerchantRepository;
import com.paymentconcentrator.cp.repository.PaymentTypeRepository;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final MerchantMapper merchantMapper = new MerchantMapper();
    private final PaymentTypeMapper paymentTypeMapper = new PaymentTypeMapper();
    private static final Logger logger = LoggerFactory.getLogger(MerchantService.class);

    public MerchantDTO create(MerchantDTO dto) throws Exception {
        if (merchantRepository.existsByEmail(dto.getEmail())) {
            throw new Exception("Email taken");
        }
        Merchant merchant = new Merchant(null, dto.getEmail(), UUID.randomUUID(), dto.getMerchantPassword(), null,
                new ArrayList<>(), new ArrayList<>());
        merchant = merchantRepository.save(merchant);
        logger.info("Merchant registered. ID: "+merchant.getId());
        return merchantMapper.toDTO(merchant);
    }

    public MerchantDTO login(MerchantDTO dto) throws Exception {
        Merchant merchant = merchantRepository.findByEmail(dto.getEmail());
        if (merchant == null || !merchant.getMerchantPassword().equals(dto.getMerchantPassword())) {
            throw new Exception("Invalid credentials");
        }
        return merchantMapper.toDTO(merchant);
    }

    public List<PaymentDto> getPaymentTypes(String id) {
        return paymentTypeMapper.toDTOList(merchantRepository.findByMerchantId(UUID.fromString(id)).getPayments());
    }

    @Transactional(rollbackFor=Exception.class)
    public PaymentDto addPaymentTypeBank(String id, MerchantBankConnectDTO dto) {
        Merchant merchant = merchantRepository.findByMerchantId(UUID.fromString(id));
        PaymentType paymentType = paymentTypeRepository.findByName(dto.getName());
        if (paymentType == null) {
            paymentType = new PaymentType(null, dto.getName(), dto.getUrl(), new ArrayList<>());
            paymentType = paymentTypeRepository.save(paymentType);
        }
        MerchantBankConnectRequestDTO merchantBankConnectRequestDTO = new MerchantBankConnectRequestDTO();
        merchantBankConnectRequestDTO.setCardHolderName(dto.getCardHolderName());
        merchantBankConnectRequestDTO.setExpDate(dto.getExpDate());
        merchantBankConnectRequestDTO.setNumber(dto.getNumber());
        merchantBankConnectRequestDTO.setSecurityCode(dto.getSecurityCode());
        merchantBankConnectRequestDTO.setMerchantId(merchant.getMerchantId());
        merchantBankConnectRequestDTO.setMerchantPassword(merchant.getMerchantPassword());
        BankClient bankClient = Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(BankClient.class, dto.getUrl() + "/api/accounts/merchant-connect");
        bankClient.forwardMerchantToBank(merchantBankConnectRequestDTO);

        merchant.getPayments().add(paymentType);
        merchantRepository.save(merchant);
        logger.info("Merchant ID: "+merchant.getId()+" has been connected to bank at "+dto.getUrl());
        return paymentTypeMapper.toDTO(paymentType);
    }
}
