# ForumHub API

ForumHub API is a RESTful application built with Java 24 and Spring Boot, designed for user registration, course creation, topic management, and authentication using JWT.  
The API uses MySQL as its database, supports environment configuration via Dotenv, and features interactive documentation with Swagger/OpenAPI.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Requirements](#requirements)
- [Getting Started](#getting-started)
- [Environment Variables](#environment-variables)
- [API Documentation](#api-documentation)
- [Authentication and Route Access](#authentication-and-route-access)
- [Endpoints & Examples](#endpoints--examples)
- [Project Structure](#project-structure)
- [Database Migrations](#database-migrations)
- [Testing Structure](#testing-structure)
- [About this project](#about-this-project)
- [Contact](#contact)
- [License](#license)

---

## Features

- User registration (public endpoint)
- JWT-based authentication via `/auth`
- Course creation (protected endpoint)
- Full CRUD for topics (authentication required)
- Interactive API documentation (Swagger/OpenAPI)

---

## Tech Stack

- Java 24
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- MySQL
- Dotenv
- Swagger/OpenAPI (springdoc-openapi)
- Maven
- JUnit 5 (testing)
- [Flyway](https://flywaydb.org/) (database migrations)

---

## Requirements

- Java 24 installed
- Maven 3.8+ installed
- MySQL running and accessible

---

## Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/ezbueno/one-alura-forumhub-api.git
   cd one-alura-forumhub-api
   ```

2. **Configure environment variables:**  
   Copy `.env.example` to `.env` and edit with your local settings:
   ```bash
   cp .env.example .env
   ```

3. **Install dependencies:**
   ```bash
   mvn clean install
   ```

4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```
   The API will start at `http://localhost:8080`

---

## Environment Variables

Set your environment variables in the `.env` file.

**Example:**
```env
# Database
spring.datasource.url=jdbc:mysql://<HOST>:<PORT>/<DB_NAME>
spring.datasource.username=<DB_USER>
spring.datasource.password=<DB_PASSWORD>
```
> **Do not** expose your real database credentials in public repositories.

You may also need to set JWT and other properties as required by your environment.

---

## API Documentation

Access the interactive Swagger UI at:

- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

All endpoints, models, and request/response examples can be viewed and tested here.

---

## Authentication and Route Access

- **Public endpoints:**  
  - `POST /users` – Register a new user  
  - `POST /auth` – Authenticate and obtain a JWT token

- **Protected endpoints (JWT required):**  
  - All `/courses` and `/topics` routes require a valid JWT token in the `Authorization: Bearer <token>` header.

**How to authenticate:**  
1. Register a user via `/users`
2. Authenticate via `/auth` with your credentials to receive a JWT token
3. Use this token for all authenticated requests (see examples below)

---

## Endpoints & Examples

### 1. Register a User (No authentication required)

**POST** `/users`

**Request:**
```json
{
  "name": "Ezandro Bueno",
  "email": "ezandro@example.com",
  "password": "123456"
}
```
**Response:**
```json
{
  "id": 1,
  "name": "Ezandro Bueno",
  "email": "ezandro@example.com"
}
```

---

### 2. Authenticate (No authentication required)

**POST** `/auth`

**Request:**
```json
{
  "email": "ezandro@example.com",
  "password": "123456"
}
```
**Response:**
```json
{
  "token": "<JWT_TOKEN>"
}
```
> The `token` field contains a JWT to be used in the `Authorization` header for protected routes.

---

### 3. Create a Course (JWT required)

**POST** `/courses`  
**Header:** `Authorization: Bearer <JWT_TOKEN>`

**Request:**
```json
{
  "name": "Spring Boot Essentials",
  "category": "Back-end"
}
```
**Response:**
```json
{
  "id": 1,
  "name": "Spring Boot Essentials",
  "category": "Back-end",
  "userId": 1,
  "userName": "Ezandro Bueno",
  "userEmail": "ezandro@example.com"
}
```

---

### 4. Create a Topic (JWT required)

**POST** `/topics`  
**Header:** `Authorization: Bearer <JWT_TOKEN>`

**Request:**
```json
{
  "title": "Topic title",
  "message": "Topic new message",
  "courseId": 1
}
```
**Response:**
```json
{
  "id": 1,
  "title": "Topic title",
  "message": "Topic new message",
  "creationDate": "2025-06-21T19:11:06.834638671",
  "status": "OPEN",
  "authorName": "Ezandro Bueno",
  "courseName": "Spring Boot Essentials"
}
```

---

### 5. List Topics (JWT required)

**GET** `/topics`  
**Header:** `Authorization: Bearer <JWT_TOKEN>`

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "title": "Topic title",
      "message": "Topic new message",
      "creationDate": "2025-06-21T19:11:07",
      "status": "OPEN",
      "authorName": "Ezandro Bueno",
      "courseName": "Spring Boot Essentials"
    }
  ],
  "pageable": { ... },
  "totalPages": 1,
  "totalElements": 1,
  "last": true,
  "size": 10,
  "number": 0,
  "numberOfElements": 1,
  "first": true,
  "empty": false
}
```

---

### 6. Get Topic by ID (JWT required)

**GET** `/topics/1`  
**Header:** `Authorization: Bearer <JWT_TOKEN>`

**Response:**
```json
{
  "id": 1,
  "title": "Topic title",
  "message": "Topic new message",
  "creationDate": "2025-06-21T19:11:07",
  "status": "OPEN",
  "authorName": "Ezandro Bueno",
  "courseName": "Spring Boot Essentials"
}
```

---

### 7. Update Topic (JWT required)

**PUT** `/topics/1`  
**Header:** `Authorization: Bearer <JWT_TOKEN>`

**Request:**
```json
{
  "title": "Topic title",
  "message": "Topic new message",
  "status": "CLOSED"
}
```
**Response:**
```json
{
  "id": 1,
  "title": "Topic title",
  "message": "Topic new message",
  "creationDate": "2025-06-21T19:11:07",
  "status": "CLOSED",
  "authorName": "Ezandro Bueno",
  "courseName": "Spring Boot Essentials"
}
```

---

### 8. Delete Topic (JWT required)

**DELETE** `/topics/1`  
**Header:** `Authorization: Bearer <JWT_TOKEN>`

**Response:**  
HTTP 204 No Content

---

## Project Structure

```
one-alura-forumhub-api
│
├── src
│   └── main
│       └── java
│           └── developer.ezandro.forumhubapi
│               ├── config                  # Global configurations (OpenAPI/Swagger, etc.)
│               ├── controller              # REST controllers (endpoints)
│               ├── dto                     # Data Transfer Objects
│               ├── exception               # Exception handling
│               ├── infra.security          # Security configurations and filters
│               ├── model                   # JPA Entities
│               ├── repository              # JPA Repositories
│               └── service                 # Business logic
│
│       └── resources
│           └── db
│               └── migration
│                   ├── V1__create_tables_user_course_topic.sql
│                   ├── V2__create_tables_profile_and_user_profile.sql
│                   ├── V3__insert_profile_user.sql
│                   ├── V4__add_user_id_to_course.sql
│                   └── V5__add_user_to_course_constraint.sql
│
└── test
    └── java
        └── developer.ezandro.forumhubapi
            ├── controller
            │   ├── AuthenticationControllerTest
            │   ├── CourseControllerTest
            │   ├── TopicControllerTest
            │   └── UserControllerTest
            ├── repository
            │   ├── ProfileRepositoryTest
            │   └── UserRepositoryTest
            └── service
                ├── CourseServiceTest
                ├── CustomUserDetailsServiceTest
                ├── TopicServiceTest
                └── UserServiceTest
```

---

## Database Migrations

The database schema is managed via [Flyway](https://flywaydb.org/) migrations under `src/main/resources/db/migration/`:

- `V1__create_tables_user_course_topic.sql`
- `V2__create_tables_profile_and_user_profile.sql`
- `V3__insert_profile_user.sql`
- `V4__add_user_id_to_course.sql`
- `V5__add_user_to_course_constraint.sql`

---

## Testing Structure

Automated tests are organized as follows:

- `controller` – Controller layer tests
- `repository` – Repository layer tests
- `service` – Service layer tests

JUnit 5 is used for all automated testing.

---

## About this project

This project was developed as a challenge for the Java Backend Developer training program offered by Oracle Next Education in partnership with Alura.

---

## Contact

- Developer: **Ezandro Bueno**
- GitHub: [https://github.com/ezbueno](https://github.com/ezbueno)

---

## License

This project is licensed under the [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0).
