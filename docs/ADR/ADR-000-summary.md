# OMS Architecture Decision Summary

Status: Accepted
Last updated: 2026-02-20

## Purpose

This document summarizes the main architecture decisions currently reflected in the OMS codebase.

It focuses on the decisions that shape the existing backend structure, module boundaries, application flow, error handling, persistence, cross-module communication, and UI-facing data updates.

## System Context

OMS is a hospitality order management system for restaurants, cafes, and bars.

The backend is a Spring Boot application with PostgreSQL persistence, REST APIs, application events, and WebSocket updates for the frontend UI.

Current core areas in the codebase include:

* `tables`: table layout, capacity, placement, occupancy, and service status.
* `visits`: active customer visits connected to tables.
* `reservations`: reservation lifecycle, seating, expiration, and walk-in blocking.
* `ui.tables_board`: UI-facing read model and WebSocket updates for the frontend tables board.
* `common`: shared technical building blocks such as exceptions, events, and configuration.
* `ui.common`: shared UI-facing WebSocket infrastructure.

## 1. Domain-Driven Design

Decision: The main business areas are modeled with DDD-inspired domain objects and value objects.

Reason: Tables, visits, and reservations contain business rules that belong inside the domain model instead of being scattered across controllers, application services, or persistence code.

Current examples:

* `tables` owns table capacity, placement, occupancy, and service-status rules.
* `visits` owns visit lifecycle rules.
* `reservations` owns reservation timing, seating, expiration, cancellation, and walk-in blocking rules.

Domain objects and value objects keep business state valid. Application services mainly coordinate use cases and delegate business decisions to the domain model.

## 2. Modular Monolith

Decision: The backend is structured as a modular monolith.

Reason: Clear business boundaries are needed, while a single deployable backend keeps development, testing, and runtime operation simpler than multiple services.

Business modules are kept separated. Cross-module access is done through explicit APIs or published events instead of directly using another module's internal services, repositories, JPA entities, or domain objects.

Spring Modulith package metadata is used to make these module boundaries explicit.

The current structure also allows a future transition to a multi-module Maven setup if stronger build-time separation becomes necessary.

## 3. Ports and Adapters / Hexagonal Architecture

Decision: Application use cases are organized with a Ports and Adapters / Hexagonal Architecture style.

Reason: Application logic stays independent from Spring MVC, JPA, WebSocket infrastructure, and another module's internal implementation.

Typical flow:

```text
REST controller
  -> request DTO
  -> command/query
  -> use case port
  -> application service
  -> domain model
  -> output port
  -> infrastructure adapter
```

Application services depend on ports. Infrastructure adapters implement those ports for persistence, event publishing, WebSocket publishing, or cross-module access.

## 4. Business Error Signaling

Decision: Expected business and application failures are signaled with typed exceptions from the layer where the problem is detected.

Reason: Validation and business errors are kept close to the rule they belong to. This avoids duplicating the same checks in controllers and domain objects, while still keeping exception creation consistent across the backend.

Domain objects and value objects throw exceptions when an invalid value or invalid state is detected. Application services throw exceptions when a use case cannot continue, such as when a required resource is missing or a business rule blocks the requested action.

Each module can expose small exception factory classes for its own application and domain errors. These factories centralize error codes and exception construction without moving the actual business validation away from the layer that owns it.

Current exception categories include invalid values, missing resources, and business conflicts.

## 5. Exception Handling

Decision: Business, application, and framework-level validation errors are handled centrally and translated into consistent API error responses.

Reason: Controllers stay focused on HTTP flow instead of manually handling expected errors. Centralized handling keeps API errors predictable and reusable across modules.

Typed exceptions are converted into structured `ProblemDetail` responses at the REST boundary. Spring MVC validation errors, where used, are handled through the same response style with field-level error details.

Error codes are resolved through message resource files. This keeps API error identifiers stable while keeping user-facing messages centralized and easier to change.

Current HTTP mapping:

* invalid value errors -> `422 Unprocessable Content`
* missing resource errors -> `404 Not Found`
* business conflict errors -> `409 Conflict`
* request validation errors -> `400 Bad Request`
* concurrency conflicts -> `409 Conflict`

## 6. Command and Query Separation

Decision: Commands and queries are separated at the application layer.

Reason: Write operations and read operations have different responsibilities. Commands change the system state, while queries return data for the UI or external clients.

Current structure includes packages such as:

```text
application.port.in.command
application.port.in.query
application.service.command
application.service.query
```

This is a CQRS-style organization, but not full CQRS with separate databases. The backend currently runs as one Spring Boot application with one PostgreSQL database.

Commands are used for actions such as creating reservations, changing table placement, opening visits, or updating table service status. Queries are used for reading data such as table details, reservation information, or UI-facing views.

## 7. Persistence and Transactions

Decision: Persistence is handled through module-owned adapters and PostgreSQL-backed JPA repositories.

Reason: Each module controls how its own data is stored and loaded, while application services stay focused on use-case coordination.

Persistence adapters translate between domain/application models and database models. This keeps JPA entities inside the infrastructure layer instead of exposing them directly to the domain, application, or REST API.

Transactions are used around write operations that change business state. Where consistency matters, critical operations can use database locking to protect the system from conflicting concurrent changes.

## 8. Event-Based Reactions

Decision: Cross-module reactions are handled through application events.

Reason: Some parts of the system need to react after a business action is completed, without creating direct dependencies between modules.

Current examples:

* table events update UI-facing projections.
* table capacity or service-status changes can affect reservation attentions.
* reservation events update UI-facing projections.
* visit events update UI-facing projections.

Events describe completed facts, such as a table being moved, a reservation being created, or a visit being opened. They are used for reactions and projections, not as a hidden command mechanism.

## 9. UI Read Models

Decision: UI-facing screens can be supported by dedicated read models that are updated from application events.

Reason: Some frontend views need data from multiple business areas. Loading that data through many separate endpoints would make the frontend more complex and could create unnecessary backend and network traffic.

Dedicated read models prepare data in a frontend-friendly format. This reduces avoidable API calls, keeps frontend-specific needs away from the core domain modules, and avoids rebuilding the same view from multiple API responses.

Read models are mainly exposed through GET endpoints for initial loading, page reloads, or bulk data loading. After the initial state is loaded, later changes can be delivered through real-time updates instead of repeatedly fetching unchanged data.

Read models are used for presentation-focused data. Core business rules remain inside the owning business modules and domain models.

## 10. Real-Time UI Updates

Decision: Important UI changes can be pushed to the frontend through WebSocket messages.

Reason: Some screens need to reflect changes quickly after table, visit, or reservation actions. Manual refreshes or constant polling would make the UI less responsive and could create unnecessary request/response traffic.

WebSocket updates are sent after relevant business changes are completed and the UI-facing read model has been updated. This lets the frontend receive only the changes it needs instead of repeatedly reloading larger views.

The backend remains the source of truth. The frontend first loads prepared read-model data through GET endpoints, then updates its displayed state from WebSocket messages instead of recalculating business rules locally.
