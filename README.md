# URL Shortener API

A REST API for creating and resolving shortened URLs.

## Features

- Create short URLs
- Generate unique 6-character short codes
- Redirect short URLs to the original URL
- URL validation
- 404 handling for unknown short URLs
- PostgreSQL persistence
- Unit and controller tests

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- JUnit 5
- Mockito

## API Endpoints

### Create Short URL

```http
POST /api/urls?url=https://www.google.com
