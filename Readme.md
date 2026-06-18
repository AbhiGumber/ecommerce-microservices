E-Commerce Microservices Application

Overview

This project is a simple e-commerce application built using Spring Boot and a microservices architecture.

The application consists of the following services:

* Product Service
* Order Service
* Notification Service
* API Gateway
* Eureka Discovery Server
* Config Server

The purpose of this project is to demonstrate microservice communication, centralized configuration, service discovery, API gateway routing, JWT-based authentication, and fault tolerance using Resilience4J.

⸻

Architecture

All client requests go through the API Gateway.

The Gateway validates JWT tokens and routes requests to the appropriate service using Eureka Service Discovery.

When an order is placed:

1. The request reaches the Gateway.
2. Order Service calls Product Service to verify that the product exists.
3. If the product is valid, the order is stored in the database.
4. Order Service then calls Notification Service.
5. Notification Service simulates sending a notification by logging a message.

Config Server provides centralized configuration for all services, while Eureka handles service registration and discovery.

⸻

Technologies Used

* Java 21
* Spring Boot 3
* Spring Cloud
* Spring Security
* Spring Cloud Gateway
* Eureka Service Discovery
* Spring Cloud Config
* PostgreSQL
* Resilience4J
* OpenAPI / Swagger
* Maven
* Docker Compose

⸻

Services

Product Service

Responsible for managing product information.

Endpoints:

* GET /products
* GET /products/{id}
* POST /products

Database:

* PostgreSQL

⸻

Order Service

Responsible for managing customer orders.

Endpoints:

* GET /orders
* GET /orders/{id}
* POST /orders

Features:

* Product validation through Product Service
* Resilience4J Circuit Breaker for fault tolerance

Database:

* PostgreSQL

⸻

Notification Service

Responsible for sending notifications when an order is placed.

Endpoints:

* POST /notifications

For simplicity, notifications are written to the application logs.

⸻

API Gateway

Responsibilities:

* Single entry point for all requests
* JWT token validation
* Routing requests to services
* Integration with Eureka Service Discovery

⸻

Security

Spring Security OAuth2 Resource Server is used at the Gateway layer.

A JWT token is generated through the authentication endpoint and validated by the Gateway before requests are forwarded to downstream services.

Public endpoints:

* /auth/**

Protected endpoints:

* /products/**
* /orders/**
* /notifications/**

⸻

Running the Application

Prerequisites

* Java 21
* Maven
* Docker Desktop

Build the Project

From the root project directory:

mvn clean package

Start the Application

docker compose up --build

Service URLs

Service	URL
Config Server	http://localhost:8888
Eureka Server	http://localhost:8761
Gateway Service	http://localhost:8080
Product Service	http://localhost:8081
Order Service	http://localhost:8082
Notification Service	http://localhost:8083

Eureka Dashboard

http://localhost:8761

⸻

Authentication

Generate a JWT token using:

POST /auth/login

Request:

{
"username": "admin",
"password": "password"
}

Use the returned token in the Authorization header:

Authorization: Bearer <token>

⸻

API Documentation

Swagger UI is available for each service:

Product Service:

http://localhost:8081/swagger-ui.html

Order Service:

http://localhost:8082/swagger-ui.html

Notification Service:

http://localhost:8083/swagger-ui.html

⸻

Design Decisions and Trade-offs

Why Microservices?

Microservices allow services to be developed, deployed, and scaled independently.

Trade-off:

* More infrastructure and operational complexity compared to a monolithic application.

Why PostgreSQL?

The application works with structured relational data such as products and orders, making PostgreSQL a good fit.

Trade-off:

* Requires schema management and database maintenance.

Why Eureka?

Eureka removes the need to hardcode service URLs and allows services to discover each other dynamically.

Trade-off:

* Introduces an additional infrastructure component.

Why API Gateway?

The Gateway centralizes routing and security concerns in a single place.

Trade-off:

* Adds an extra network hop for every request.

Why Resilience4J?

Resilience4J helps prevent cascading failures when dependent services become unavailable.

Trade-off:

* Requires additional configuration and monitoring.

⸻

Testing

The project includes:

* Unit tests for business logic
* Controller layer tests
* Integration tests for REST endpoints
* Circuit Breaker validation scenarios

Run tests using:

mvn test

⸻

Future Improvements

* Kafka-based asynchronous communication
* Role-based authorization
* Distributed tracing
* Centralized logging
* Monitoring with Prometheus and Grafana
* Redis caching

⸻

Author

Abhinav Gumber