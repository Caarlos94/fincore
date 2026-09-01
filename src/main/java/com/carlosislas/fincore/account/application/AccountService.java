package com.carlosislas.fincore.account.application;

import com.carlosislas.fincore.account.api.dto.AccountResponse;
import com.carlosislas.fincore.account.api.dto.CreateAccountRequest;
import com.carlosislas.fincore.account.domain.Account;
import com.carlosislas.fincore.account.domain.AccountStatus;
import com.carlosislas.fincore.account.infrastructure.AccountRepository;
import com.carlosislas.fincore.auth.domain.User;
import com.carlosislas.fincore.auth.infrastructure.UserRepository;
import com.carlosislas.fincore.common.error.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public AccountResponse createAccount(Long ownerId, CreateAccountRequest request) {

        User owner = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException(ownerId));

        Account account = new Account(
                owner, generateAccountNumber(), request.currency(), BigDecimal.ZERO, AccountStatus.ACTIVE);

        Account savedAccount = accountRepository.save(account);

        return new AccountResponse(
                savedAccount.getId(), savedAccount.getAccountNumber(), savedAccount.getCurrency(),
                savedAccount.getBalance(), savedAccount.getAccountStatus()
        );
    }

    private String generateAccountNumber() {
        return "ACC-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 20);
    }
}
