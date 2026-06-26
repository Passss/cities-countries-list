# Country and City Backend Service

A clean-architecture Spring Boot backend service for managing countries and cities. Only in memory
H2 datastore and is pre-populated with some sample data at application start-up. A postman collection is also provided with the endpoints with testdata. Also there are maven wrappers in case one has not installed maven locally.

## Architecture

This project strictly adheres to a **Layered Clean Architecture**:
`Controller` (Web/API layer) -> `Service` (Business logic) -> `Repository` (Data access) -> `Entity` (Database model)

- **Controller Layer**: Exposes REST API endpoints with Jakarta Validation and Springdoc OpenAPI descriptions.
- **Service Layer**: Decoupled from direct web structures, implements business logic, maps entities to DTO response structures, and maintains transactions.
- **Repository Layer**: Extends `JpaRepository` for data fetching. Uses database indexes on the name columns of both Country and City entities to optimize query performance.
- **Entity Layer**: Domain objects representing JPA entities mapped to database tables.

---

## Features

1. **REST APIs**:
   - `GET /countries` - Returns the list of all countries.
   - `GET /countries/{countryId}/cities` - Returns paginated list of cities for a selected country (`?page=0&size=10&sortBy=name&sortDir=asc`).
   - `GET /cities/{cityId}` - Returns detailed properties of a city by its ID.
   - `PUT /countries/{name}` - Creates a new Country by name.
   - `PUT /countries/{countryId}/{cityName}` - Creates a new City for the country (using default values for details).
   - `POST /countries/{countryId}/cities` - Creates a new City with validated request body (population, zip code, and description).
2. **OpenAPI / Swagger**: Fully integrated. View API documentation, structures, request-response schemas, and try out endpoints from the browser.
3. **Global Exception Handling**: Returns structured JSON error payloads for field validation errors (`400 Bad Request`), missing entities (`404 Not Found`), and server conflicts.
4. **Data Seeding**: Automatically seeds initial countries (India, United States, United Kingdom, Japan) and representative cities at startup for testing.

---

## Getting Started

### Prerequisites
- Java 21 or later
- Maven 3.9+

### Building the Project
To compile the project and run the automated unit and integration tests:
```bash
mvn clean test
```

### Running the Application
To run the Spring Boot application locally:
```bash
mvn spring-boot:run
```

---

## Running with Docker (Alternative)

To build a Docker image and run the application inside a container:

1. **Build the JAR**:
   ```bash
   mvn clean package -DskipTests
   ```

3. **Build the Docker Image** from the root directory:
   ```bash
   docker build -t country-city-service .
   ```

4. **Run the Container** from the root directory:
   ```bash
   docker run -p 8080:8080 country-city-service
   ```

---

## Accessing Documentation and Consoles

Once the application is running:
- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI Json Docs**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- **H2 Embedded Database Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - JDBC URL: `jdbc:h2:mem:countrycitydb`
  - Username: `sa`
  - Password: `password`
