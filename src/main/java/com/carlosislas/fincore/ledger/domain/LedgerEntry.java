package com.carlosislas.fincore.ledger.domain;

import com.carlosislas.fincore.account.domain.Account;
import com.carlosislas.fincore.transfer.domain.Transfer;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ledger_entries")
public class LedgerEntry {

    protected LedgerEntry() {
    }

    public LedgerEntry(
            Transfer transfer,
            Account account,
            EntryType entryType,
            BigDecimal amount,
            BigDecimal balanceAfter
    ) {
        this.transfer = transfer;
        this.account = account;
        this.entryType = entryType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.createdAt = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id", nullable = false)
    private Transfer transfer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "entry_type", length = 20, nullable = false)
    private EntryType entryType;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "balance_after", precision = 19, scale = 2, nullable = false)
    private BigDecimal balanceAfter;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public Account getAccount() {
        return account;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
