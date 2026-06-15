# OMS — Order Management System

A hospitality order management system (restaurants, cafes, bars etc.)

---

## Modules

| Module | Responsibility |
| --- | --- |
| `tables` | Table layout, capacity, occupancy, and service status |
| `visits` | Active customer visits connected to tables |
| `reservations` | Reservation lifecycle, seating, expiration, and walk-in blocking |
| `orders` | Customer orders connected to active visits |
| `products` | Product information used by order-related flows |
| `ui.tables_board` | UI-facing read model and WebSocket updates for the tables board |
| `common` | Shared exceptions, events, and configuration |
| `ui.common` | Shared WebSocket infrastructure |

---

## Architecture

OMS is a modular monolith with clear business boundaries and a single deployable backend.

Key decisions:

- **Hexagonal Architecture** — use cases depend on ports; infrastructure stays at the edges.
- **Domain-Driven Design** — business rules live in domain objects and value objects.
- **Command/Query Separation** — write and read use cases are organized separately.
- **Application Events** — completed facts are published as events for projections and cross-module reactions.
- **Dedicated Read Models** — UI-facing data is prepared separately from domain models.
- **WebSocket Updates** — table board changes are pushed to the frontend in real time.
- **Central Error Handling** — business and validation errors translate into consistent RFC 9457 `ProblemDetail` responses.
- **Database-Level Consistency** — transactions, constraints, indexes, and pessimistic locking protect critical flows.
- **Scheduled Background Work** — time-based reservation actions run through scheduled application use cases.

For detailed decisions, see [`docs/adr/ADR-000-OMS-Architecture-Decision-Summary.md`](docs/adr/ADR-000-OMS-Architecture-Decision-Summary.md).

---

## Tech Stack

| Area | Technology |
| --- | --- |
| Backend | Java 21, Spring Boot |
| Architecture | Modular Monolith, Hexagonal Architecture, DDD |
| Persistence | PostgreSQL, Spring Data JPA, Hibernate, Flyway |
| Module metadata | Spring Modulith |
| Events | Spring application events |
| Real-time | Spring WebSocket, STOMP |
| API errors | RFC 9457 Problem Details |
| Frontend | React, TypeScript, Vite |
| Tooling | Maven, Docker |

---

## Backend Flow

Command flow:

```text
REST Controller → Request DTO → Command → Use Case Port → Application Service → Domain Model → Output Port → Infrastructure Adapter
```

Event reaction flow:

```text
Application Service → Event Publisher Port → Spring Event Publisher Adapter → Application Event → Module Listener → Projection or Cross-Module Reaction
```

---

## Getting Started

### Requirements

- Java 21
- Maven
- Docker (for PostgreSQL)
- Node.js and npm (for the frontend)

### Backend

```bash
git clone <repository-url>
cd oms
docker compose up -d
mvn spring-boot:run
```


---

## Testing

```bash
mvn clean test
```

Tests cover application use cases, domain rules, module boundaries, exception handling, and important backend flows. Architecture tests verify that modules do not access each other's internals.

---

## Documentation

```text
docs/
└── adr/
    └── ADR-000-OMS-Architecture-Decision-Summary.md
```

---

## License

Personal learning and portfolio project.
