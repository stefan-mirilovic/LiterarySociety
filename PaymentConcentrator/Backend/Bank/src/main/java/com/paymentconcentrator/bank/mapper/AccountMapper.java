package com.paymentconcentrator.bank.mapper;

import com.paymentconcentrator.bank.dto.AccountDTO;
import com.paymentconcentrator.bank.model.Account;
import com.paymentconcentrator.bank.model.Card;

public class AccountMapper implements MapperInterface<Account, AccountDTO> {
    @Override
    public Account toEntity(AccountDTO dto) {
        return null;
    }

    @Override
    public AccountDTO toDto(Account entity) {
        if (entity.getCards().size() == 0) {
            return new AccountDTO(entity.getId(), entity.getFirstName(), entity.getLastName(), "",
                    "", "", entity.getFunds());
        }
        Card card = entity.getCards().get(entity.getCards().size()-1);
        return new AccountDTO(entity.getId(), entity.getFirstName(), entity.getLastName(), card.getNumber(),
                card.getSecurityCode(), card.getExpDate(), entity.getFunds());
    }

    public AccountDTO toDtoWithCard(Account entity, Card card) {
        return new AccountDTO(entity.getId(), entity.getFirstName(), entity.getLastName(), card.getNumber(),
                card.getSecurityCode(), card.getExpDate(), entity.getFunds());
    }
}
