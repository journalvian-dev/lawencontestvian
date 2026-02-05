Order Management Backend

A simple backend system built with Spring Boot to manage items, inventories, and orders while maintaining stock consistency.

## Tech Stack

Java 17

Spring Boot

Spring Data JPA

Hibernate

MySQL / PostgreSQL (adjust based on your config)

JUnit 5

Mockito

## Architecture

This project follows a layered architecture:

Controller → Service → Repository

Why?
To ensure separation of concerns and maintain clean business logic.

## Features
Item Management

Create item

Update item

Delete item

Search items

Inventory Management

Stock top-up

Stock withdrawal

Insufficient stock validation

Order Management

Create order with automatic stock deduction

Update order with stock adjustment

Delete order with automatic restock

## Key Design Decisions
- Stock Protection

Orders and withdrawals are validated to prevent negative inventory.

- Transactional Safety

Critical operations use @Transactional to maintain data integrity.

- DTO Pattern

DTOs are used to decouple API contracts from database entities.

- Global Exception Handling

Provides consistent error responses.

## Testing

Unit tests cover:

Service layer business rules

Controller endpoints

Frameworks used:

JUnit 5

Mockito

MockMvc

## How to Run
mvn clean install
mvn spring-boot:run
