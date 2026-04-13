# Architecture Design Document

## Architectural Overview

The system follows a layered monolithic architecture with a clear separation of concerns, optimized for both internal management (MVC) and external/modular scalability (REST).

### 1. Presentation Layer (The Dual-Frontend Approach)

The application manages two distinct interaction models through specialized controllers:

**Server-Side Rendering (SSR)**: Uses Spring MVC and Thymeleaf for the Admin and Doctor dashboards. This provides a secure, session-based environment for internal staff where complex state management is handled by the server.

**Stateless API**: Employs REST Controllers for all other modules, facilitating future-proof integration with mobile apps or modern JavaScript frameworks (like React or Vue).

### 2. Logic & Service Layer

To prevent code duplication, a **Common Service Layer** serves as the central orchestration point.

**Abstraction**: Controllers interact only with Services, never directly with repositories.

**Logic Consolidation**: Business rules (e.g., appointment scheduling constraints or prescription validation) are enforced in one place, ensuring consistent behavior whether the request comes from the UI or an API.

### 3. Polyglot Persistence Layer

The system treats data storage based on the nature of the information:

| Database | Technology | Purpose |	Why? |
|----------|------------|---------|------|
| MySQL    | Spring Data JPA / Hibernate | Core Entities (Admin, Doctor, Patient, Appointment) | Ensures ACID compliance and maintains strict relationships for structured scheduling and identity data. |
| MongoDB |	Spring Data MongoDB | Prescriptions | Provides schema flexibility for varied medical records and scales better for high-volume, document-centric data. |

### Key Advantages

**Scalability**: The RESTful components allow parts of the system to scale independently.

**Flexibility**: Adding new types of prescription data doesn't require complex MySQL migrations.

**Maintainability**: The shared service layer ensures that a change in business logic is reflected across both the web dashboards and the API modules simultaneously.

## Request Flow Pattern

1. **Client Request**: A user interacts with a Thymeleaf page or hits a REST endpoint.

2. **Controller Dispatch**: Spring Boot routes the request to either an @Controller (MVC) or @RestController.

3. **Service Invocation**: The controller passes data to the Common Service Layer.

4. **Repository Delegation**: The Service calls the JPA Repository (MySQL) for relational data or the Mongo Repository (MongoDB) for document data.

5. **Database access**: Each repository interfaces directly with the underlying database engine.

6. **Response Mapping**: Once data is retrieved from the database, it is mapped back through DTOs to the client.

7. **Data in use**:
- In MVC flows, models are passed from the controller to Thymeleaf templates, where they are rendered as dynamic HTML for the browser.
- In REST flows, the same models (or transformed DTOs) are serialized into JSON and sent back to the client as part of an HTTP response.