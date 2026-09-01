package com.carlosislas.fincore.account.api;

import com.carlosislas.fincore.account.api.dto.AccountResponse;
import com.carlosislas.fincore.account.api.dto.CreateAccountRequest;
import com.carlosislas.fincore.account.application.AccountService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@RequestHeader("X-User-Id") Long ownerId,
            @Valid @RequestBody CreateAccountRequest request) {
        return accountService.createAccount(ownerId, request);
    }
}
