package com.carlosislas.fincore.auth.infrastructure;

import com.carlosislas.fincore.auth.domain.Role;
import com.carlosislas.fincore.auth.domain.User;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest // Loads only the persistence/JPA part of the application for the test
@Testcontainers // Enables Testcontainers support for this JUnit test
@ImportAutoConfiguration(FlywayAutoConfiguration.class) // Makes Flyway run so the temporary DB gets the real schema
class UserRepositoryTest {

    // @Testcontainers + @Container → "Start a temporary PostgreSQL container for this test."
    @Container // Marks the PostgreSQL container that Testcontainers should start/stop
    @ServiceConnection // Connects Spring automatically to that temporary PostgreSQL container
    static PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:17"); // Creates a temporary real PostgreSQL instance

    @Autowired // Injects the Spring-managed UserRepository object
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveUser() {
        User user = new User("test@fincore.com", "hashed-password", Role.USER);
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());

        entityManager.flush();
        entityManager.clear();

        User foundUser = userRepository
                .findById(savedUser.getId())
                .orElseThrow();

        assertEquals("test@fincore.com", foundUser.getEmail());
        assertEquals("hashed-password", foundUser.getPasswordHash());
        assertEquals(Role.USER, foundUser.getRole());
    }
}
