package com.paymentconcentrator.cp.mapper;

import com.paymentconcentrator.cp.dto.PaymentDto;
import com.paymentconcentrator.cp.model.PaymentType;

import java.util.ArrayList;
import java.util.List;

public class PaymentTypeMapper {

    public PaymentDto toDTO(PaymentType entity) {
        return new PaymentDto(entity.getId(), entity.getName(), entity.getUrl());
    }

    public List<PaymentDto> toDTOList(List<PaymentType> entities) {
        List<PaymentDto> dtos = new ArrayList<>();
        for (PaymentType entity: entities) {
            dtos.add(toDTO(entity));
        }
        return dtos;
    }
}
