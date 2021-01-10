package com.paymentconcentrator.cp.mapper;

import com.paymentconcentrator.cp.dto.MerchantDTO;
import com.paymentconcentrator.cp.model.Merchant;

public class MerchantMapper {

    public MerchantDTO toDTO(Merchant entity) {
        return new MerchantDTO(entity.getId(), entity.getEmail(), entity.getMerchantId(), entity.getMerchantPassword());
    }
}
