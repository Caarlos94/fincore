package com.carlosislas.fincore.auth.infrastructure;

import com.carlosislas.fincore.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
