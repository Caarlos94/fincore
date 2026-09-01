package com.carlosislas.fincore.account.api.dto;

import com.carlosislas.fincore.account.domain.Currency;
import jakarta.validation.constraints.NotNull;

public record CreateAccountRequest(@NotNull Currency currency) {
}
