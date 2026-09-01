package com.carlosislas.fincore.account.api.dto;

import com.carlosislas.fincore.account.domain.AccountStatus;
import com.carlosislas.fincore.account.domain.Currency;

import java.math.BigDecimal;

public record AccountResponse(Long id, String accountNumber, Currency currency, BigDecimal balance,
                              AccountStatus status) {
}
