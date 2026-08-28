package com.carlosislas.fincore.account.infrastructure;

import com.carlosislas.fincore.account.domain.Account;
import com.carlosislas.fincore.account.domain.AccountStatus;
import com.carlosislas.fincore.account.domain.Currency;
import com.carlosislas.fincore.auth.domain.Role;
import com.carlosislas.fincore.auth.domain.User;
import com.carlosislas.fincore.auth.infrastructure.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Testcontainers
@ImportAutoConfiguration(FlywayAutoConfiguration.class)
class AccountRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:17");

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveAccount() {
        User user = new User("test@fincore.com", "hashed-password", Role.USER);
        User savedUser = userRepository.save(user);

        Account account = new Account(
                savedUser,
                "ACC-0001",
                Currency.MXN,
                new BigDecimal("1000.00"),
                AccountStatus.ACTIVE
        );
        Account savedAccount = accountRepository.save(account);

        assertNotNull(savedUser.getId());
        assertNotNull(savedAccount.getId());

        entityManager.flush();
        entityManager.clear();

        User foundUser = userRepository
                .findById(savedUser.getId())
                .orElseThrow();

        Account foundAccount = accountRepository
                .findById(savedAccount.getId())
                .orElseThrow();

        assertEquals("test@fincore.com", foundUser.getEmail());
        assertEquals("hashed-password", foundUser.getPasswordHash());
        assertEquals("ACC-0001", foundAccount.getAccountNumber());
        assertEquals(0, new BigDecimal("1000.00").compareTo(foundAccount.getBalance()));
        assertEquals(Currency.MXN, foundAccount.getCurrency());
        assertEquals(AccountStatus.ACTIVE, foundAccount.getAccountStatus());
        assertEquals(foundUser.getId(), foundAccount.getOwner().getId());
        assertNotNull(foundAccount.getVersion());
    }
}