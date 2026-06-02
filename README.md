# Healthcare Management System

A comprehensive Healthcare Management System built with **Spring Boot**, designed using a **Layered Monolithic Architecture** that combines **Spring MVC**, **REST APIs**, **MySQL**, and **MongoDB** to provide a scalable, maintainable, and future-ready platform for managing healthcare operations.

---

## Overview

This application supports healthcare administration through two interaction models:

* **Server-Side Rendered (SSR) Web Interface** for internal users such as Administrators and Doctors.
* **RESTful APIs** for external integrations, mobile applications, and modern frontend frameworks.

The system follows clean architecture principles with a strong separation of concerns, centralized business logic, and polyglot persistence.

---

## Architecture

### High-Level Architecture

```text
┌─────────────────────────┐
│        Clients          │
│                         │
│  Browser (Thymeleaf)    │
│  Mobile App             │
│  React/Vue Frontend     │
└──────────┬──────────────┘
           │
           ▼
┌─────────────────────────┐
│   Presentation Layer    │
│                         │
│  @Controller (MVC)      │
│  @RestController (API)  │
└──────────┬──────────────┘
           │
           ▼
┌─────────────────────────┐
│ Common Service Layer    │
│                         │
│ Business Logic          │
│ Validation Rules        │
│ Workflow Orchestration  │
└──────────┬──────────────┘
           │
           ▼
┌─────────────────────────┐
│ Repository Layer        │
│                         │
│ JPA Repositories        │
│ Mongo Repositories      │
└───────┬───────┬─────────┘
        │       │
        ▼       ▼
   MySQL DB   MongoDB
```

---

## Architecture Components

### 1. Presentation Layer

The system supports two frontend approaches.

#### Spring MVC + Thymeleaf

Used for:

* Administrator Dashboard
* Doctor Dashboard

Features:

* Server-side rendering
* Session-based authentication
* Secure internal operations
* Reduced client-side complexity

#### REST API

Used for:

* Mobile applications
* Third-party integrations
* Future SPA frontends (React, Vue, Angular)

Features:

* Stateless communication
* JSON responses
* Scalable architecture
* Easy external integration

---

### 2. Service Layer

The Service Layer acts as the central orchestration point of the application.

Responsibilities:

* Business logic implementation
* Validation rules
* Transaction management
* Data transformation
* Workflow coordination

Examples:

* Appointment scheduling validation
* Prescription processing
* Doctor availability checks
* Patient registration rules

Benefits:

* Prevents code duplication
* Ensures consistency across MVC and REST modules
* Improves maintainability

---

### 3. Persistence Layer

The application uses a polyglot persistence strategy.

#### MySQL (Relational Database)

Managed through:

* Spring Data JPA
* Hibernate ORM

Stores:

* Admins
* Doctors
* Patients
* Appointments
* User relationships

Advantages:

* ACID compliance
* Referential integrity
* Strong relational modeling

#### MongoDB (Document Database)

Managed through:

* Spring Data MongoDB

Stores:

* Prescriptions
* Medical records
* Flexible healthcare documents

Advantages:

* Schema flexibility
* Better handling of complex documents
* Easier evolution of medical record structures

---

## Technology Stack

### Backend

* Java 17+
* Spring Boot
* Spring MVC
* Spring Web
* Spring Data JPA
* Spring Data MongoDB
* Hibernate

### Frontend

* Thymeleaf
* HTML5
* CSS3
* Bootstrap
* JavaScript

### Databases

* MySQL
* MongoDB

### Build Tool

* Maven

### Development Tools

* IntelliJ IDEA / Eclipse
* Postman
* MySQL Workbench
* MongoDB Compass

---

## Project Structure

```text
src/main/java
│
├── controller
│   ├── mvc
│   └── api
│
├── service
│
├── repository
│   ├── mysql
│   └── mongo
│
├── entity
│
├── dto
│
├── config
│
├── exception
│
└── util

src/main/resources
│
├── templates
│
├── static
│
├── application.properties
│
└── application.yml
```

---

## Core Modules

### Administration Module

Features:

* Manage doctors
* Manage patients
* Appointment monitoring
* System administration

### Doctor Module

Features:

* View appointments
* Access patient information
* Create prescriptions
* Update treatment details

### Patient Module

Features:

* Registration
* Appointment booking
* Medical history access
* Prescription retrieval

### Appointment Module

Features:

* Appointment scheduling
* Availability management
* Booking validation
* Appointment tracking

### Prescription Module

Features:

* Digital prescriptions
* Medical record storage
* Flexible prescription schema
* Historical prescription access

---

## Request Flow

### MVC Flow

```text
Browser
   │
   ▼
@Controller
   │
   ▼
Service Layer
   │
   ▼
Repository
   │
   ▼
Database
   │
   ▼
Service Layer
   │
   ▼
Model
   │
   ▼
Thymeleaf Template
   │
   ▼
HTML Response
```

### REST API Flow

```text
Client
   │
   ▼
@RestController
   │
   ▼
Service Layer
   │
   ▼
Repository
   │
   ▼
Database
   │
   ▼
DTO Mapping
   │
   ▼
JSON Response
```

---

## Data Flow

### MVC Requests

1. User interacts with a Thymeleaf page.
2. Spring MVC routes the request.
3. Business logic executes in the service layer.
4. Data is retrieved from MySQL or MongoDB.
5. Models are passed to Thymeleaf.
6. Dynamic HTML is rendered and returned.

### REST Requests

1. Client sends an HTTP request.
2. REST Controller receives the request.
3. Service layer processes business rules.
4. Repository retrieves data.
5. Entities are converted into DTOs.
6. JSON response is returned.

---

## Key Architectural Advantages

### Scalability

* REST APIs support external systems and mobile clients.
* Components can evolve independently.

### Maintainability

* Shared service layer centralizes business logic.
* Reduced code duplication.

### Flexibility

* MongoDB allows evolving prescription structures without major schema migrations.

### Consistency

* Business rules are enforced uniformly across MVC and REST interfaces.

### Security

* Internal dashboards operate through server-side sessions.
* REST endpoints can be secured independently using token-based authentication.

---

## Future Enhancements

* Spring Security with JWT Authentication
* Role-Based Access Control (RBAC)
* Email and SMS notifications
* Telemedicine support
* Audit logging
* Docker containerization
* Kubernetes deployment
* API documentation with Swagger/OpenAPI
* Integration with Electronic Health Record (EHR) systems

---

## Getting Started

### Prerequisites

* Java 17+
* Maven
* MySQL Server
* MongoDB Server

### Installation

```bash
# Clone repository
git clone <repository-url>

# Navigate to project
cd healthcare-management-system

# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

### Configure Databases

Update `application.properties`:

```properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/healthcare_db
spring.datasource.username=root
spring.datasource.password=password

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/healthcare_db
```

---

## License

This project is intended for educational and learning purposes. Modify and distribute according to your organization's licensing requirements.
