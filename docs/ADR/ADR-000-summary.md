# ADR-000: OMS Architecture Decision Summary

Status: Accepted  
Last updated: 2026-06-15

## Purpose

This document summarizes the main architecture decisions currently reflected in the OMS codebase.

It focuses on the decisions that shape the backend structure, module boundaries, application flow, null-safety, error handling, persistence, concurrency, cross-module communication, event-based reactions, time-based business transitions, and UI-facing data updates.

## System Context

OMS is a hospitality order management system for restaurants, cafes, and bars.

The backend is a Java 21 Spring Boot application with PostgreSQL persistence, REST APIs, application events, Spring Modulith module metadata, scheduled time-based work, and WebSocket updates for the frontend UI.

The architecture is currently designed around business areas such as:

* `tables`: table layout, capacity, placement, occupancy, and service status.
* `visits`: active customer visits connected to tables.
* `reservations`: reservation lifecycle, seating, expiration, and walk-in blocking.
* `orders`: customer orders connected to active visits.
* `products`: product information used by order-related flows.
* `ui.tables_board`: UI-facing read model and WebSocket updates for the frontend tables board.
* `common`: shared technical building blocks such as exceptions, events, and configuration.
* `ui.common`: shared UI-facing WebSocket infrastructure.

## 1. Domain-Driven Design

Decision: The main business areas are modeled with DDD-inspired domain objects and value objects.

Reason: Tables, visits, reservations, and orders contain business rules that belong inside the domain model instead of being scattered across controllers, application services, or persistence code.

Current examples:

* `tables` owns table capacity, placement, occupancy, and service-status rules.
* `visits` owns visit lifecycle rules.
* `reservations` owns reservation timing, seating, expiration, cancellation, and walk-in blocking rules.
* `orders` owns order lifecycle and order state rules.

Domain objects and value objects are responsible for keeping business state valid. Application services mainly coordinate use cases and delegate business decisions to the domain model.

## 2. Modular Monolith

Decision: The backend is structured as a modular monolith.

Reason: The system needs clear business boundaries, while a single deployable backend keeps development, testing, deployment, and runtime operation simpler than multiple services.

Business modules are separated by package structure and explicit module-facing APIs. Cross-module access is done through named interfaces, application-facing APIs, or published events instead of directly using another module's internal services, repositories, JPA entities, or domain objects.

Spring Modulith package metadata is used to make module boundaries explicit. The application uses Spring Modulith annotations such as `@Modulithic`, `@ApplicationModule`, and `@NamedInterface` to describe module structure and allowed module-facing APIs.

The current structure also allows a future transition to a multi-module Maven setup if stronger build-time separation becomes necessary.

## 3. Ports and Adapters / Hexagonal Architecture

Decision: Application use cases are organized with a Ports and Adapters / Hexagonal Architecture style.

Reason: Application logic should stay independent from Spring MVC, JPA, WebSocket infrastructure, event infrastructure, scheduler infrastructure, and other modules’ internal implementations.

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

Application services depend on ports. Infrastructure adapters implement those ports for persistence, event publishing, WebSocket publishing, scheduling, or cross-module access.

This keeps technical frameworks at the edges of the application and keeps the core use cases focused on business flow.

## 4. Null-Safety

Decision: The backend uses package-level null-safety defaults with `@NullMarked`.

Reason: Null-related bugs can easily appear in application services, ports, DTO mappings, and domain code. A non-null-by-default style makes method contracts clearer and reduces accidental null handling mistakes.

The root application package is marked with `@NullMarked`, so types are treated as non-null by default unless nullability is explicitly declared.

Values that are expected to exist are modeled as non-null. Optional or missing values need to be represented intentionally.

## 5. Business Error Signaling

Decision: Expected business and application failures are signaled with typed exceptions from the layer where the problem is detected.

Reason: Validation and business errors should stay close to the rule they belong to. This avoids duplicating the same checks in controllers and domain objects, while still keeping exception creation consistent across the backend.

Domain objects and value objects throw exceptions when an invalid value or invalid state is detected. Application services throw exceptions when a use case cannot continue, such as when a required resource is missing or a business rule blocks the requested action.

Some modules expose small exception factory classes for their own application and domain errors. These factories centralize error codes and exception construction without moving the actual business validation away from the layer that owns it.

Current exception categories include:

* invalid values
* missing resources
* business conflicts
* concurrency conflicts

## 6. Exception Handling

Decision: Business, application, and framework-level validation errors are handled centrally and translated into consistent API error responses.

Reason: Controllers should stay focused on HTTP flow instead of manually handling expected errors. Centralized handling keeps API errors predictable and reusable across modules.

Typed exceptions are converted into structured `ProblemDetail` responses at the REST boundary. Spring MVC validation errors, where used, are handled through the same response style with field-level error details.

Error codes are resolved through message resource files. This keeps API error identifiers stable while keeping user-facing messages centralized and easier to change.

Current HTTP mapping:

* invalid value errors -> `422 Unprocessable Content`
* missing resource errors -> `404 Not Found`
* business conflict errors -> `409 Conflict`
* request validation errors -> `400 Bad Request`
* concurrency conflicts -> `409 Conflict`

## 7. Command and Query Separation

Decision: Commands and queries are separated at the application layer.

Reason: Write operations and read operations have different responsibilities. Commands change system state, while queries return data for the UI or external clients.

Current structure includes packages such as:

```text
application.port.in.command
application.port.in.query
application.service.command
application.service.query
```

This is a CQRS-style organization, but not full CQRS with separate databases. The backend currently runs as one Spring Boot application with one PostgreSQL database.

Commands are used for actions such as creating reservations, changing table placement, opening visits, updating table service status, and changing order state.

Queries are used for reading data such as table details, reservation information, order information, or UI-facing views.

## 8. Persistence, Transactions, and Concurrency

Decision: Persistence is handled through module-owned adapters and PostgreSQL-backed JPA repositories, with transactions and database-level consistency controls around state-changing operations.

Reason: Each module should control how its own data is stored and loaded, while application services stay focused on use-case coordination.

Persistence adapters translate between domain/application models and database models. This keeps JPA entities inside the infrastructure layer instead of exposing them directly to the domain, application, or REST API.

The backend uses Spring Data JPA repositories for persistence and `@Transactional` boundaries around use cases that read or change business state.

For critical state changes, repositories can use pessimistic database locking through Spring Data JPA `@Lock` with `LockModeType.PESSIMISTIC_WRITE`.

This is used to protect sensitive operations where concurrent requests could otherwise update the same business aggregate at the same time, such as a table, visit, reservation, or order.

Database constraints and PostgreSQL indexes are also used where they can protect persistence-level consistency.

Concurrency conflicts are treated as expected application-level failures and are translated into consistent API error responses when they reach the REST boundary.

## 9. Synchronous Cross-Module Communication

Decision: Some cross-module reads are handled through explicit synchronous module APIs.

Reason: Not every module interaction needs to be event-based. Some use cases need immediate information from another business area before the command can continue, such as checking whether a table, visit, or product can be used.

These interactions are exposed through explicit module-facing APIs or Spring Modulith named interfaces instead of directly accessing another module’s internal repositories, services, JPA entities, or domain objects.

Current examples include order-related access to product or visit information.

Synchronous APIs are used for direct questions during a use case. Events are used for completed facts and later reactions.

## 10. Event-Based Reactions

Decision: Cross-module reactions and UI-facing projections are handled through application events.

Reason: Some parts of the system need to react after a business action is completed, without creating direct dependencies between modules or forcing the original command to know every downstream effect.

Events are published through an application-level event publisher port, `BaseEventPublisher`, instead of exposing Spring’s `ApplicationEventPublisher` directly to the domain or application use cases.

The Spring adapter, `SpringModulithEventPublisher`, implements that port and delegates event publication to Spring’s `ApplicationEventPublisher`.

Typical flow:

```text
application service
  -> BaseEventPublisher
  -> SpringModulithEventPublisher
  -> ApplicationEventPublisher
  -> @ApplicationModuleListener
  -> projection or cross-module reaction
```

Current examples:

* table events update UI-facing projections.
* reservation events update UI-facing projections.
* visit events update UI-facing projections.
* table capacity or service-status changes can trigger reservation attention flags.

Events describe completed facts, such as a table being moved, a reservation being created, a reservation attention flag being added, or a visit being opened.

Events are used for reactions and projections, not as a hidden command mechanism.

The event-based flow is separate from synchronous cross-module APIs. Synchronous APIs answer immediate questions during a use case, while events notify other parts of the system after a change has already happened.

## 11. Time-Based Business Transitions

Decision: Time-based business actions are handled through scheduled work.

Reason: Some business actions do not happen immediately when a command is executed. They need to happen later based on business time or lifecycle rules.

Examples include:

* reservation expiration
* walk-in block start
* restoring scheduled reservation work after application startup

The backend uses Spring scheduling infrastructure, including `TaskScheduler`, for time-based follow-up work.

Scheduled jobs call application use case ports instead of directly changing persistence state. This keeps scheduled work aligned with the same application flow used by normal commands.

Scheduled work can be registered, cancelled, or restored when needed. Startup recovery can re-register active scheduled work when the application becomes ready.

This keeps time-based behavior explicit without putting timer logic inside controllers or domain objects.

## 12. UI Read Models

Decision: UI-facing screens can be supported by dedicated read models that are updated from application events.

Reason: Some frontend views need data from multiple business areas. Loading that data through many separate endpoints would make the frontend more complex and could create unnecessary backend and network traffic.

Dedicated read models prepare data in a frontend-friendly format. This reduces avoidable API calls, keeps frontend-specific needs away from the core domain modules, and avoids rebuilding the same view from multiple API responses.

Read models are mainly exposed through GET endpoints for initial loading, page reloads, or bulk data loading.

After the initial state is loaded, later changes can be delivered through real-time updates instead of repeatedly fetching unchanged data.

Read models are used for presentation-focused data. Core business rules remain inside the owning business modules and domain models.

## 13. Real-Time UI Updates

Decision: Important UI changes can be pushed to the frontend through WebSocket messages.

Reason: Some screens need to reflect changes quickly after table, visit, or reservation actions. Manual refreshes or constant polling would make the UI less responsive and could create unnecessary request/response traffic.

The backend uses Spring WebSocket/STOMP infrastructure for real-time table board updates.

WebSocket updates are sent after relevant business changes are completed and the UI-facing read model has been updated.

The frontend first loads prepared read-model data through GET endpoints. After that, it updates its displayed state from WebSocket messages instead of recalculating business rules locally.

The backend remains the source of truth.

The frontend may use local validation or preview logic for better user experience, but final validation and business decisions remain on the backend.
