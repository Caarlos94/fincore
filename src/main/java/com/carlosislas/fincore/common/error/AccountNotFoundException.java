package com.carlosislas.fincore.common.error;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(Long AccountId) {
        super("Account not found with id: " + AccountId);
    }

}
