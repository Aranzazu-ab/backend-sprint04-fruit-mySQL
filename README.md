# Fruit Management REST API (MySQL)

A RESTful API built with Spring Boot to manage fruit inventory and supplier traceability. This service allows you to manage providers, track fruit stock, associate each fruit product with its supplier, and query fruits filtered by provider.

The application connects to a MySQL database using Spring Data JPA, validates input data with Bean Validation, provides centralized exception handling, and is fully containerized using Docker and Docker Compose.

---

## Business Logic and Practical Example

The core of this application is the relationship between suppliers (`Provider`) and products (`Fruit`). Each fruit must be linked to an existing supplier through a JPA `@ManyToOne` association.

### Practical Workflow Walkthrough

Here is a common scenario showing how the API behaves:

#### 1. Register a Provider
First, create a supplier. The provider's name is unique and required, along with their country of origin.

- **Request:** `POST /providers`
```json
{
  "name": "Mercabarna",
  "country": "Spain"
}
```
- **Response:** `201 Created`
```json
{
  "id": 1,
  "name": "Mercabarna",
  "country": "Spain"
}
```

#### 2. Register Fruit Linked to the Provider
When adding a fruit, you must supply `providerId`. The API validates that the provider exists before persisting the fruit.

- **Request:** `POST /fruits`
```json
{
  "name": "Golden Apple",
  "weightInKg": 150,
  "providerId": 1
}
```
- **Response:** `201 Created`
```json
{
  "id": 1,
  "name": "Golden Apple",
  "weightInKg": 150,
  "providerId": 1,
  "providerName": "Mercabarna"
}
```

If you attempt to create a fruit with a non-existent `providerId` (e.g., ID `99`), the API returns `404 Not Found` with a message explaining that the provider does not exist.

#### 3. Filter Fruits by Provider
You can retrieve all fruits supplied by a specific provider using a query parameter.

- **Request:** `GET /fruits?providerId=1`
- **Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Golden Apple",
    "weightInKg": 150,
    "providerId": 1,
    "providerName": "Mercabarna"
  }
]
```

#### 4. Delete Provider Protection Rule
To preserve data integrity, the system prevents the deletion of any provider that currently has associated fruits.

- **Request:** `DELETE /providers/1`
- **Response:** `400 Bad Request`
```json
{
  "error": "Cannot delete provider with ID 1 because it has associated fruits"
}
```
A provider can only be deleted (`204 No Content`) once all their associated fruits have been deleted or reallocated.

---

## Tech Stack

- **Language:** Java 17 / 21
- **Framework:** Spring Boot 3
- **Data & Persistence:** Spring Data JPA, Hibernate, MySQL 8
- **Validation:** Jakarta Bean Validation (`@Valid`, `@NotBlank`, `@Positive`, `@NotNull`)
- **Testing:** JUnit 5, Mockito, Spring Boot Test, MockMvc
- **Containerization:** Docker (multi-stage build), Docker Compose
- **Build Tool:** Maven

---

## Project Architecture

The project follows a standard layered architecture with clear separation of concerns:

- `controllers`: REST endpoints handling incoming HTTP requests, status codes, and routing.
- `services`: Business logic, domain rules, and coordination between repositories.
- `repository`: Spring Data JPA interfaces for database operations.
- `model`: JPA entities (`Fruit` and `Provider`) defining schema and relationships.
- `dto`: Request and response record objects ensuring internal entities are never exposed directly.
- `mapper`: Mapping components converting between entities and DTOs.
- `exception`: Custom exceptions and a `GlobalExceptionHandler` returning consistent, structured error responses.

---

## API Endpoints Reference

### Providers (`/providers`)

| Method | Endpoint | Description | Status Codes |
|---|---|---|---|
| POST | `/providers` | Register a new provider | 201 Created, 400 Bad Request |
| GET | `/providers` | List all registered providers | 200 OK |
| GET | `/providers/{id}` | Get provider details by ID | 200 OK, 404 Not Found |
| PUT | `/providers/{id}` | Update provider name or country | 200 OK, 400 Bad Request, 404 Not Found |
| DELETE | `/providers/{id}` | Delete provider (if no fruits attached) | 204 No Content, 400 Bad Request, 404 Not Found |

### Fruits (`/fruits`)

| Method | Endpoint | Description | Status Codes |
|---|---|---|---|
| POST | `/fruits` | Create a fruit associated with a provider | 201 Created, 400 Bad Request, 404 Not Found |
| GET | `/fruits` | List all fruits | 200 OK |
| GET | `/fruits?providerId={id}` | List fruits filtered by provider ID | 200 OK, 404 Not Found |
| GET | `/fruits/{id}` | Get fruit details by ID | 200 OK, 404 Not Found |
| PUT | `/fruits/{id}` | Update fruit name, weight, or provider | 200 OK, 400 Bad Request, 404 Not Found |
| DELETE | `/fruits/{id}` | Delete fruit by ID | 204 No Content, 404 Not Found |

---

## Automated Testing

The project includes automated tests to verify business logic and API endpoints:

- **Unit Tests (`src/test/.../services`):** Built with JUnit 5 and Mockito. These isolate service logic by mocking repository and mapper interactions to test scenarios such as duplicate provider names, missing entity handling, and deletion restrictions.
- **Integration Tests (`src/test/.../controllers`):** Built using `@SpringBootTest` and `MockMvc`. These execute real HTTP requests through the controller layer against the Spring application context to validate status codes, request validation, serialization, and database persistence.

To run the complete test suite:

```bash
./mvnw test
```

---

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+ (or use the included `./mvnw` wrapper)
- Docker and Docker Compose (recommended) or a local MySQL server

### Running with Docker Compose (Recommended)

The easiest way to run both the application and the MySQL database is using Docker Compose:

```bash
docker compose up --build
```

This will:
1. Spin up a MySQL 8 container with health checks enabled.
2. Build the Spring Boot application using a multi-stage Dockerfile.
3. Start the application on port `8080` once the database is healthy.

To stop the services:
```bash
docker compose down
```

### Running Locally

1. Ensure MySQL is running locally on port 3306 with a database named `fruit_db`.
2. Check or update database credentials in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/fruit_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=root
   ```
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
4. The API will be accessible at `http://localhost:8080`.

---

## Environment Variables

When running in containerized or staging environments, database connection parameters can be configured via environment variables:

| Variable | Description | Default (Local) |
|---|---|---|
| `SPRING_DATASOURCE_URL` | JDBC URL for MySQL connection | `jdbc:mysql://localhost:3306/fruit_db` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `root` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `root` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | DDL schema generation mode | `update` |
