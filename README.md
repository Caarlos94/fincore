# FinCore

FinCore is a backend fintech project built to practice and demonstrate real Java backend engineering concepts.

## Tech Stack

- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate / JPA
- PostgreSQL
- Flyway
- Maven
- Docker
- JUnit 5
- Testcontainers
- Mockito
- MockMvc

## Current Architecture

FinCore is currently implemented as a modular monolith using a package-by-feature structure.

```text
com.carlosislas.fincore
├── account
│   ├── api
│   ├── application
│   ├── domain
│   └── infrastructure
├── auth
│   ├── domain
│   └── infrastructure
└── common
    └── error