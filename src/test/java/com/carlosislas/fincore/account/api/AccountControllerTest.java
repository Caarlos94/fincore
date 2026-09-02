package com.carlosislas.fincore.account.api;

import com.carlosislas.fincore.account.api.dto.AccountResponse;
import com.carlosislas.fincore.account.api.dto.CreateAccountRequest;
import com.carlosislas.fincore.account.application.AccountService;
import com.carlosislas.fincore.account.domain.AccountStatus;
import com.carlosislas.fincore.account.domain.Currency;
import com.carlosislas.fincore.common.error.AccountNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AccountController.class) // Carga solo la capa web
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simula requests HTTP contra el controller

    @MockitoBean // Reemplaza AccountService por un mock
    private AccountService accountService;

    @Test
    void shouldReturnAccounts() throws Exception {

        AccountResponse account = new AccountResponse(
                1L,
                "ACC-123",
                Currency.MXN,
                new BigDecimal("1000.00"),
                AccountStatus.ACTIVE
        );

        when(accountService.getAccountsByOwner(1L)) // Define qué debe devolver el mock
                .thenReturn(List.of(account));

        mockMvc.perform( // Ejecuta la request simulada
                        get("/api/v1/accounts")
                                .header("X-User-Id", 1L)
                ).andExpect(status().isOk()) // Verifica status y JSON
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].accountNumber").value("ACC-123"))
                .andExpect(jsonPath("$[0].currency").value("MXN"))
                .andExpect(jsonPath("$[0].balance").value(1000.00))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));

    }

    @Test
    void shouldReturnAccountByAccountId() throws Exception {

        AccountResponse account = new AccountResponse(
                1L,
                "ACC-123",
                Currency.MXN,
                new BigDecimal("1000.00"),
                AccountStatus.ACTIVE
        );

        when(accountService.getAccountById(1L, 10L))
                .thenReturn(account);

        mockMvc.perform(
                        get("/api/v1/accounts/{accountId}", 1L)
                                .header("X-User-Id", 10L)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.accountNumber").value("ACC-123"))
                .andExpect(jsonPath("$.currency").value("MXN"))
                .andExpect(jsonPath("$.balance").value(1000.00))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

    }

    @Test
    void shouldCreateAccount() throws Exception {

        CreateAccountRequest request = new CreateAccountRequest(Currency.MXN);
        AccountResponse account = new AccountResponse(
                1L,
                "ACC-123",
                Currency.MXN,
                new BigDecimal("0.00"),
                AccountStatus.ACTIVE
        );

        when(accountService.createAccount(1L, request))
                .thenReturn(account);

        mockMvc.perform(
                        post("/api/v1/accounts")
                                .header("X-User-Id", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "currency": "MXN"
                                        }
                                        """)
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.accountNumber").value("ACC-123"))
                .andExpect(jsonPath("$.currency").value("MXN"))
                .andExpect(jsonPath("$.balance").value(0.00))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldReturnBadRequestWhenCurrencyIsNull() throws Exception {

        mockMvc.perform(
                        post("/api/v1/accounts")
                                .header("X-User-Id", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "currency": null
                                        }
                                        """)
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Failed"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldReturnNotFoundWhenAccountDoesNotExist() throws Exception {

        when(accountService.getAccountById(1L, 10L))
                .thenThrow(new AccountNotFoundException(1L));

        mockMvc.perform(
                        get("/api/v1/accounts/{accountId}", 1L)
                                .header("X-User-Id", 10L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Account Not Found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail")
                        .value("Account not found with id: 1"));
    }
}
