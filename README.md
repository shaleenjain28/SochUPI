# SochUPI 💸

> A Behavioral Fintech App that helps users become aware of their spending habits through intelligent nudges and strict budget tracking.

![Status](https://img.shields.io/badge/Status-Phase%202%20(Security)%20Complete-success)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)

## 🏗️ System Architecture

SochUPI is built with a highly scalable, enterprise-grade backend architecture:

* **Layered Architecture:** Strict separation of concerns (Controller → Service → Repository → Entity).
* **Financial Integrity:** All monetary values use `BigDecimal` with exact precision mapping.
* **Stateless Security:** Secured via JSON Web Tokens (JWT) using Spring Security 6.
* **Data Transfer Objects (DTO):** Immutable Java 14 `record` types used for all Request/Response payloads, secured with Jakarta Bean Validation.
* **Centralized Exception Handling:** `@RestControllerAdvice` ensures all APIs return a predictable, standardized error JSON structure.

## 🚀 Current Progress & Features

### Phase 1: Minimum Viable Product (Completed)
- [x] **User Management:** Registration with BCrypt password hashing.
- [x] **Budget System:** Create monthly budgets, set savings targets, and toggle budget locks.
- [x] **Transaction Tracking:** Log expenses against specific budgets with exact date/category mapping.
- [x] **Exception Handling:** Global error handling mapping domain exceptions to proper HTTP status codes (400, 403, 404, 409).

### Phase 2: Security & Authentication (Completed)
- [x] **JWT Generation & Validation:** `JwtService` with HMAC-SHA256 signing.
- [x] **Security Filter Chain:** Custom `OncePerRequestFilter` intercepting and validating Bearer tokens.
- [x] **Identity Extraction:** Extracted identity securely via `@AuthenticationPrincipal` to prevent URL-parameter manipulation.

### Phase 3: Performance & Scalability (In Progress)
- [ ] Redis Caching for Dashboard Summaries
- [ ] Composite Database Indexing
- [ ] HikariCP Connection Pooling Tuning
- [ ] Load Testing (JMeter)

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| **Backend Framework** | Java 17, Spring Boot 3.2.5 |
| **Database & ORM** | MySQL 8, Spring Data JPA, Hibernate |
| **Security** | Spring Security 6, JJWT, BCrypt |
| **Validation** | Jakarta Bean Validation (`spring-boot-starter-validation`) |
| **Frontend** | React Native (Expo + TypeScript) - *Pending* |

## ⚙️ Getting Started

### Prerequisites
- Java 17+
- Maven
- MySQL 8+

### Running the Backend Locally
1. Clone the repository.
2. Update `application.properties` with your MySQL credentials.
3. Run the application:
```bash
cd backend
mvn spring-boot:run
```
The API will be available at `http://localhost:8080`.

---
*Developed with a focus on clean code, financial data integrity, and scalable system design.*
