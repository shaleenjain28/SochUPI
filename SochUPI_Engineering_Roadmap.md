# SochUPI — Engineering Roadmap & Resume Evidence Tracker

> Every bullet on your resume should have a real feature behind it.  
> This document maps **system design concepts → SochUPI features → resume claims**.

---

## Current State (What's Built)

```
✅ User Registration + BCrypt hashing
✅ Budget CRUD (Create, List, Toggle Lock)
✅ Layered Architecture (Controller → Service → Repository)
✅ Spring Data JPA with derived queries
✅ Jakarta Bean Validation
✅ DTO pattern (Request/Response separation)
✅ MySQL + Hibernate auto-DDL
```

---

## Phase 1 — Core Features (Complete the MVP)

> **Goal**: A fully functional budgeting + transaction tracking backend.

### 1.1 Transaction System
| What to build | Concept learned |
|---------------|-----------------|
| Transaction Entity (`amount`, `category`, `description`, `date`, linked to Budget & User) | **Entity relationships** (`@ManyToOne`), **enum-based categorization** |
| Transaction Repository (find by user, by budget, by date range, by category) | **Derived queries**, **custom JPQL queries** |
| Transaction Service (add transaction, validate against budget, compute remaining balance) | **Business logic**, **domain validation** |
| Transaction Controller (`POST`, `GET` with filters) | **REST API design**, **query filtering** |

### 1.2 Global Exception Handling
| What to build | Concept learned |
|---------------|-----------------|
| `@ControllerAdvice` + custom exception classes (`ResourceNotFoundException`, `DuplicateResourceException`, `UnauthorizedException`) | **Centralized error handling**, **proper HTTP error codes** (400, 401, 403, 404, 409) |
| Standardized error response DTO (`ErrorResponse` with timestamp, status, message, path) | **API contract consistency** |

**Resume claim this enables:**
> *"Built standardized REST APIs with centralized exception handling, returning structured error responses with proper HTTP semantics (400/401/404/409)."*

### 1.3 Pagination & Sorting
| What to build | Concept learned |
|---------------|-----------------|
| Paginated transaction listing (`GET /api/transactions?page=0&size=20&sort=date,desc`) | **`Pageable`** in Spring Data, **cursor vs offset pagination** trade-offs |
| Paginated budget history | Same pattern, reusable |

**Resume claim this enables:**
> *"Implemented paginated APIs with configurable sorting, reducing payload size by ~80% on list endpoints."*

---

## Phase 2 — Security & Authentication

> **Goal**: Production-grade auth. No more passing `userId` in query params.

### 2.1 JWT Authentication
| What to build | Concept learned |
|---------------|-----------------|
| Login endpoint (`POST /api/auth/login`) → returns access token + refresh token | **Stateless authentication**, **token-based auth** |
| JWT filter (`OncePerRequestFilter`) that validates token on every request | **Filter chain**, **Spring Security architecture** |
| Refresh token flow (short-lived access token + long-lived refresh token stored in DB) | **Token rotation**, **session management** |
| Extract `userId` from token in service layer (no more `@RequestParam`) | **SecurityContext**, **principal injection** |

**Resume claim this enables:**
> *"Implemented stateless JWT authentication with refresh token rotation, securing all API endpoints through Spring Security's filter chain."*

### 2.2 Role-Based Access Control (RBAC)
| What to build | Concept learned |
|---------------|-----------------|
| User roles (`STUDENT`, `PARENT`, `ADMIN`) | **Authorization vs Authentication** |
| `@PreAuthorize` annotations on endpoints | **Method-level security** |
| Parent can view child's budget (read-only) | **Resource-level permissions** |

**Resume claim this enables:**
> *"Implemented RBAC-based workflows supporting multiple user roles (Student, Parent, Admin) with method-level authorization."*

---

## Phase 3 — Performance & Scalability

> **Goal**: Make the numbers on your resume REAL.

### 3.1 Redis Caching
| What to build | Concept learned |
|---------------|-----------------|
| Cache the dashboard summary (total spent, remaining budget, daily limit) | **Cache-aside pattern**, **TTL-based expiration** |
| Invalidate cache when a new transaction is added | **Cache invalidation strategies** |
| `@Cacheable`, `@CacheEvict` annotations | **Spring Cache abstraction** |

**How to measure:**
- Before caching: hit the dashboard endpoint 100 times → measure avg response time
- After caching: same test → compare
- Target: **60-70% reduction in dashboard data retrieval latency**

**Resume claim this enables:**
> *"Integrated Redis caching layer with TTL-based expiration, reducing dashboard data retrieval latency by ~65% across KPI and reporting endpoints."*

### 3.2 Database Indexing & Query Optimization
| What to build | Concept learned |
|---------------|-----------------|
| Add composite indexes on `(user_id, month, year)` in budgets table | **Database indexing**, **composite indexes** |
| Add index on `(user_id, transaction_date)` in transactions table | **Query execution plans**, **EXPLAIN ANALYZE** |
| Use `@Query` with optimized JPQL for aggregation (sum of transactions per category) | **N+1 query problem**, **batch fetching** |

**How to measure:**
- Run `EXPLAIN ANALYZE` before and after indexing
- Measure query time on 10,000+ rows
- Target: **sub-50ms query execution on indexed columns**

**Resume claim this enables:**
> *"Optimized MySQL queries and indexing strategies, achieving sub-50ms query execution across transaction and budget retrieval endpoints."*

### 3.3 Connection Pooling (HikariCP)
| What to build | Concept learned |
|---------------|-----------------|
| Configure HikariCP pool size, connection timeout, idle timeout in `application.properties` | **Connection pooling**, **resource management** |
| Monitor pool metrics via Spring Boot Actuator | **Observability** |

### 3.4 Load Testing
| What to build | Concept learned |
|---------------|-----------------|
| Write JMeter or Gatling scripts to simulate 100+ concurrent users | **Load testing**, **throughput measurement** |
| Measure p50, p95, p99 response times | **Percentile-based SLAs** |
| Target: **sub-200ms p95 response time for core endpoints** | **Performance benchmarking** |

**Resume claim this enables:**
> *"Load-tested APIs with JMeter, achieving sub-200ms p95 response times under 100+ concurrent users."*

---

## Phase 4 — Production Infrastructure

> **Goal**: Deployable, observable, maintainable.

### 4.1 Docker & Containerization
| What to build | Concept learned |
|---------------|-----------------|
| `Dockerfile` for Spring Boot app | **Containerization**, **multi-stage builds** |
| `docker-compose.yml` (app + MySQL + Redis) | **Service orchestration**, **networking** |
| Environment-based config (`application-dev.yml`, `application-prod.yml`) | **Profile-based configuration** |

### 4.2 CI/CD Pipeline (GitHub Actions)
| What to build | Concept learned |
|---------------|-----------------|
| On PR: run tests + lint + build | **Continuous Integration** |
| On merge to main: build Docker image + push to registry | **Continuous Delivery** |
| Optionally deploy to AWS EC2 or Railway | **Cloud deployment** |

### 4.3 Database Migrations (Flyway)
| What to build | Concept learned |
|---------------|-----------------|
| Replace `ddl-auto=update` with versioned SQL migration scripts | **Schema versioning**, **safe production deployments** |
| Rollback strategy for failed migrations | **Data integrity** |

**Resume claim this enables:**
> *"Containerized services with Docker Compose (App + MySQL + Redis), deployed via GitHub Actions CI/CD pipeline with Flyway-managed database migrations."*

### 4.4 Structured Logging & Monitoring
| What to build | Concept learned |
|---------------|-----------------|
| SLF4J + Logback with JSON-formatted logs | **Structured logging** |
| Spring Boot Actuator (`/health`, `/metrics`, `/info`) | **Health checks**, **observability** |
| Request/response logging interceptor with timing | **Middleware pattern**, **performance monitoring** |

---

## Phase 5 — Advanced Features (Behavioral Layer)

> **Goal**: The features that make SochUPI unique, not just another CRUD app.

### 5.1 Scheduled Jobs (Month-End Processing)
| What to build | Concept learned |
|---------------|-----------------|
| `@Scheduled` cron job that runs on the last day of every month | **Scheduling**, **cron expressions** |
| Calculates actual savings vs target for every locked budget | **Batch processing** |
| Generates a monthly summary record | **Data aggregation** |

### 5.2 Event-Driven Nudge System
| What to build | Concept learned |
|---------------|-----------------|
| When a transaction is added → publish an `ApplicationEvent` | **Event-driven architecture**, **Observer pattern** |
| `NudgeListener` checks: is user near daily limit? Near category limit? Over budget? | **Domain event handling** |
| Generate nudge records (stored in DB, sent as push notifications later) | **Notification system design** |

**Resume claim this enables:**
> *"Designed event-driven nudge system using Spring ApplicationEvents, triggering real-time spending alerts based on configurable budget thresholds."*

### 5.3 SMS Parsing Pipeline
| What to build | Concept learned |
|---------------|-----------------|
| Regex-based parser that extracts amount, merchant, date from bank SMS | **Text processing**, **regex patterns** |
| Categorization engine (Swiggy → Food, Amazon → Shopping) | **Rule engine**, **pattern matching** |
| API endpoint for the mobile app to send raw SMS text | **Data ingestion pipeline** |

**Resume claim this enables:**
> *"Built automated SMS parsing pipeline using regex-based transaction extraction, eliminating manual expense tracking with accurate transaction categorization."*

### 5.4 Analytics & Insights API
| What to build | Concept learned |
|---------------|-----------------|
| Spending by category (pie chart data) | **Aggregation queries**, **GROUP BY** |
| Daily/weekly/monthly spend trends (line chart data) | **Time-series data** |
| Comparison with previous months | **Window functions** or application-level comparison |
| "You spend 40% more on weekends" type insights | **Data analysis logic** |

---

## Phase 6 — Scalability Patterns (Advanced)

> **Goal**: Concepts you can talk about in system design interviews, backed by real implementation.

### 6.1 API Rate Limiting
| What to build | Concept learned |
|---------------|-----------------|
| Rate limiter using Bucket4j or Redis-based sliding window | **Rate limiting algorithms** (token bucket, sliding window) |
| Per-user limits (e.g., 100 requests/minute) | **Throttling**, **fairness** |

### 6.2 API Versioning
| What to build | Concept learned |
|---------------|-----------------|
| `/api/v1/budgets` vs `/api/v2/budgets` | **Backward compatibility**, **API evolution** |

### 6.3 Async Processing
| What to build | Concept learned |
|---------------|-----------------|
| `@Async` for non-critical tasks (logging, notifications) | **Thread pools**, **async execution** |
| Optional: RabbitMQ/Kafka for SMS processing pipeline | **Message queues**, **eventual consistency** |

### 6.4 Database Read Replicas (Conceptual)
| What to build | Concept learned |
|---------------|-----------------|
| Configure a read-only datasource for GET endpoints | **Read/write splitting**, **CQRS basics** |
| Route queries using `@Transactional(readOnly = true)` | **Transaction management** |

---

## Resume Bullets — Final Version (Once Everything Is Built)

### SochUPI – Behavioral FinTech Application

> - Architected a full-stack fintech app using Spring Boot, React Native, and MySQL, integrating a behavioral confirmation layer for UPI payments that reduces impulsive spending.
> - Designed layered backend architecture (Controller → Service → Repository) with Spring Data JPA, enforcing domain constraints through custom derived queries and Jakarta Bean Validation.
> - Implemented stateless JWT authentication with refresh token rotation and RBAC-based authorization supporting Student, Parent, and Admin roles.
> - Built automated SMS parsing pipeline using regex-based transaction extraction, eliminating manual expense tracking with accurate categorization across 8+ spending categories.
> - Integrated Redis caching with TTL-based expiration, reducing dashboard data retrieval latency by ~65% across budget and analytics endpoints.
> - Optimized MySQL queries with composite indexing, achieving sub-50ms execution times; load-tested APIs with JMeter, confirming sub-200ms p95 under 100+ concurrent users.
> - Designed event-driven nudge system using Spring ApplicationEvents, triggering real-time spending alerts based on configurable budget thresholds.
> - Containerized services with Docker Compose (App + MySQL + Redis), deployed via GitHub Actions CI/CD with Flyway-managed database migrations.

---

## Skills You'll Be Able to Claim (With Proof)

| Skill Category | What SochUPI proves |
|----------------|---------------------|
| **Backend** | Spring Boot, Spring Security, Spring Data JPA, Hibernate, Maven |
| **Databases** | MySQL, Redis, indexing, query optimization, Flyway migrations |
| **Security** | JWT, BCrypt, RBAC, rate limiting |
| **System Design** | Layered architecture, caching, event-driven design, async processing, pagination |
| **DevOps** | Docker, Docker Compose, GitHub Actions CI/CD, environment profiles |
| **Testing** | Load testing (JMeter/Gatling), Postman, unit tests |
| **Architecture** | DTO pattern, clean code, API versioning, error handling |

---

> [!IMPORTANT]
> **The rule**: Only add a bullet to your resume AFTER you've built and tested that feature.  
> This document tracks what's coming — your resume only reflects what's done.
