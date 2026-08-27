package com.carlosislas.fincore.account.infrastructure;

import com.carlosislas.fincore.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
