# URL Shortener API

A RESTful URL shortening service built with Java and Spring Boot.

The application allows users to create short URLs from long URLs and resolve those short URLs through HTTP redirects.

---

## Overview

This project is a backend REST API demonstrating a typical Spring Boot application architecture.

A submitted URL is stored in PostgreSQL and assigned a randomly generated six-character short code.

Example:

```text
Original URL:
https://www.example.com/some/very/long/url

Short code:
a8F3kP

Request:
GET /api/urls/a8F3kP

Response:
302 Found
Location: https://www.example.com/some/very/long/url
```

The project focuses on clean separation between the controller, service, repository and persistence layers, as well as input validation and automated testing.

---

## Features

- Create shortened URLs
- Generate random six-character short codes
- Persist URLs in PostgreSQL
- Resolve short codes to original URLs
- HTTP 302 redirects
- URL input validation
- Global exception handling
- 404 handling for unknown short codes
- RESTful API design
- Unit tests
- Controller/API tests
- Maven build configuration

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot 4 | Application framework |
| Spring Web MVC | REST API |
| Spring Data JPA | Database access |
| Hibernate | ORM |
| PostgreSQL | Relational database |
| Maven | Build and dependency management |
| JUnit 5 | Testing |
| Mockito | Mocking and unit testing |
| Git / GitHub | Version control |

---

## Architecture

The application follows a layered architecture:

```text
                    HTTP Request
                         │
                         ▼
              ┌─────────────────────┐
              │ ShortUrlController  │
              │      REST API       │
              └──────────┬──────────┘
                         │
                         ▼
              ┌─────────────────────┐
              │   ShortUrlService   │
              │   Business Logic    │
              └──────────┬──────────┘
                         │
                         ▼
              ┌─────────────────────┐
              │ ShortUrlRepository  │
              │    Data Access      │
              └──────────┬──────────┘
                         │
                         ▼
                  ┌──────────────┐
                  │  PostgreSQL  │
                  └──────────────┘
```

### Controller

`ShortUrlController`

Responsible for:

- Handling HTTP requests
- Request parameter validation
- Returning HTTP responses
- Creating redirects

### Service

`ShortUrlService`

Responsible for:

- Creating short URLs
- Generating short codes
- Resolving short codes
- Handling business logic

### Repository

`ShortUrlRepository`

Responsible for database access through Spring Data JPA.

### Model

`ShortUrl`

Represents the persisted URL entity.

### Exception Handling

`GlobalExceptionHandler`

Provides centralized handling for application exceptions such as unknown short URLs.

---

## Project Structure

```text
url-shortener-api/
│
├── src/
│   │
│   ├── main/
│   │   │
│   │   ├── java/
│   │   │   └── com/kajiru/urlshortenerapi/
│   │   │       │
│   │   │       ├── controller/
│   │   │       │   └── ShortUrlController.java
│   │   │       │
│   │   │       ├── exception/
│   │   │       │   ├── GlobalExceptionHandler.java
│   │   │       │   └── ShortUrlNotFoundException.java
│   │   │       │
│   │   │       ├── model/
│   │   │       │   └── ShortUrl.java
│   │   │       │
│   │   │       ├── repository/
│   │   │       │   └── ShortUrlRepository.java
│   │   │       │
│   │   │       ├── service/
│   │   │       │   └── ShortUrlService.java
│   │   │       │
│   │   │       └── UrlShortenerApiApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/
│           └── com/kajiru/urlshortenerapi/
│               ├── ShortUrlControllerTest.java
│               ├── ShortUrlServiceTest.java
│               └── UrlShortenerApiApplicationTests.java
│
├── .gitignore
├── .gitattributes
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

# API

Base URL:

```text
http://localhost:8080
```

---

## Create Short URL

Creates a new shortened URL.

### Request

```http
POST /api/urls?url=https://www.google.com
```

### Example

```bash
curl -X POST "http://localhost:8080/api/urls?url=https://www.google.com"
```

### Response

```json
{
  "id": 1,
  "originalUrl": "https://www.google.com",
  "shortCode": "a8F3kP"
}
```

The generated short code is six characters long.

---

## Resolve Short URL

Resolves a short code and redirects the client to the original URL.

### Request

```http
GET /api/urls/{shortCode}
```

### Example

```http
GET /api/urls/a8F3kP
```

### Response

```http
302 Found
Location: https://www.google.com
```

The API uses an HTTP redirect instead of returning the original URL as a normal response.

---

# Validation

The API validates the URL provided when creating a short URL.

URLs must start with:

```text
http://
```

or

```text
https://
```

### Valid

```text
https://www.google.com
http://example.com
```

### Invalid

```text
google.com
example.com
abc
```

Invalid input returns:

```http
400 Bad Request
```

---

# Error Handling

The application uses a centralized exception handler.

## Unknown Short Code

Request:

```http
GET /api/urls/doesnotexist
```

Response:

```http
404 Not Found
```

```text
Short URL not found: doesnotexist
```

---

# Database

The application uses PostgreSQL for persistent storage.

The `ShortUrl` entity contains:

| Field | Type | Description |
|---|---|---|
| `id` | Long | Database identifier |
| `originalUrl` | String | Original URL |
| `shortCode` | String | Generated short identifier |

Example database record:

```text
id    original_url              short_code
------------------------------------------------
1     https://www.google.com    a8F3kP
2     https://github.com        X2m9Qa
```

---

# Running the Application

## Requirements

Before running the application, install:

- Java 21
- PostgreSQL
- Maven (optional, Maven Wrapper is included)

---

## Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE urlshortenerdb;
```

Configure the database connection in:

```text
src/main/resources/application.properties
```

Example configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/urlshortenerdb
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

Replace the username and password with your local PostgreSQL credentials.

---

## Start the Application

### Windows

Using the Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

### Linux / macOS

```bash
./mvnw spring-boot:run
```

The application starts on:

```text
http://localhost:8080
```

---

# Testing

The project contains both unit tests and controller tests.

## Service Tests

`ShortUrlServiceTest`

Tests the business logic including:

- Creating short URLs
- Resolving original URLs
- Handling unknown short codes
- Generating six-character short codes

## Controller Tests

`ShortUrlControllerTest`

Tests the REST API including:

- Creating URLs through the API
- Redirect responses
- 404 responses
- Invalid URL validation
- Empty URL validation

Run all tests with:

### Windows

```powershell
.\mvnw.cmd test
```

### Linux / macOS

```bash
./mvnw test
```

Current test coverage includes:

```text
5 controller tests
4 service tests
```

All implemented tests are passing.

---

# Example Workflow

A typical request flow looks like this:

### 1. Client submits a URL

```http
POST /api/urls?url=https://www.example.com
```

### 2. Controller validates the request

```text
ShortUrlController
        │
        ▼
URL validation
```

### 3. Service creates a short code

```text
ShortUrlService
        │
        ▼
Generate 6-character code
```

### 4. Entity is persisted

```text
ShortUrlRepository
        │
        ▼
PostgreSQL
```

### 5. API returns the generated short URL data

```json
{
  "id": 1,
  "originalUrl": "https://www.example.com",
  "shortCode": "a8F3kP"
}
```

### 6. Client requests the short code

```http
GET /api/urls/a8F3kP
```

### 7. API redirects to the original URL

```http
302 Found
Location: https://www.example.com
```

---

# Development Concepts Demonstrated

This project demonstrates practical backend development concepts including:

- REST API development
- Layered application architecture
- Dependency Injection
- Spring MVC
- Spring Data JPA
- ORM with Hibernate
- PostgreSQL persistence
- Request validation
- Exception handling
- HTTP status codes
- HTTP redirects
- Unit testing
- Mock-based testing
- Controller testing
- Maven dependency management

---

# Possible Future Improvements

Potential extensions for a production-ready version could include:

- Custom aliases for short URLs
- Expiration dates
- Click statistics
- Redirect counters
- User accounts
- Authentication and authorization
- Rate limiting
- Duplicate URL handling
- Collision prevention for generated short codes
- API documentation with OpenAPI / Swagger
- Docker containerization
- Integration tests with Testcontainers
- Environment-based configuration
- Centralized logging
- Monitoring and metrics

---

# License

This project is intended as a personal portfolio and learning project.

---

# Author

**Kajiru44**

GitHub:

https://github.com/Kajiru44
