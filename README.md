## 🧩 Hexagonal Architecture Overview

The main idea behind **Hexagonal Architecture (Ports & Adapters)** is to **isolate the domain logic from external technical concerns** such as databases, web frameworks, or messaging systems.

The domain remains at the center of the application and is accessed only through well-defined **ports**, which are implemented by **adapters**.

---

## Ports & Adapters

A **port** is simply an interface that defines how the application communicates with the outside world.

There are two types of ports:

### ➡️ Input Ports (Inbound)
- Represent **use cases** of the application
- Define what the user or external system can do
- Implemented by the **domain layer**
- Called by inbound adapters (REST controllers, message consumers, etc.)

### ⬅️ Output Ports (Outbound)
- Represent dependencies on external systems
- Used to access databases, external APIs, message brokers, etc.
- Defined in the **application layer**
- Implemented by outbound adapters in the infrastructure layer

---

## Application Layer

The **application layer** acts as a boundary around the domain.

It contains:
- Input and output ports (interfaces)
- Use case definitions

In this project, the input ports define use cases such as:
- Creating a new product
- Retrieving a product by its identifier

The output ports define contracts to interact with external systems.
For example, `ProductOutputPort` defines how product data is persisted or retrieved, without knowing how or where.

---

## Infrastructure Layer

The **infrastructure layer** represents the outer part of the hexagonal architecture.

It contains **adapters** that implement the application ports and handle technical concerns.

The `infrastructure.adapters` package is divided into:

- **config**  
  Contains Spring configuration and bean definitions

- **input**  
  Inbound adapters that drive the application  
  (e.g. REST controllers invoking input ports)

- **output**  
  Outbound adapters implementing output ports  
  (e.g. database repositories, external services)

Adapters interact with the core application **only through ports**, ensuring loose coupling.

---

## Domain Layer

The **domain layer** is the heart of the system.

It contains:
- Domain entities
- Business rules
- Domain services

This layer is completely **decoupled from both application and infrastructure layers**.
Changes in frameworks, databases, or APIs do not impact the domain unless business requirements change.

### ProductService

`ProductService` is a key domain component:
- Implements input ports (use cases)
- Uses output ports to access external systems
- Contains the core business logic

This design guarantees:
- High testability
- Strong separation of concerns
- Long-term maintainability

---

## ✅ Benefits of Hexagonal Architecture

- Clear separation between business logic and technical concerns
- Easier unit testing of domain logic
- Technology-agnostic core
- Better adaptability to change
- Improved readability and maintainability