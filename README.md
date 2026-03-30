# spring-5
# STD24081

# TD5 - Spring Boot REST API

## Description
Refactoring of a console application into a REST API using Spring Boot and JDBC.

## Tech Stack
- Java
- Spring Boot
- PostgreSQL
- JDBC


## Environment Variables
```
JDBC_URL=jdbc:postgresql://localhost:5432/your_database
USER=your_username
PASSWORD=your_password
```

# Endpoints

### Ingredients
| Method | URL                                                 | Description                     | Status          |
|--------|-----------------------------------------------------|---------------------------------|-----------------|
| GET    | `/ingredients`                                      | Get all ingredients             | 200             |
| GET    | `/ingredients/{id}`                                 | Get ingredient by id            | 200 / 404       |
| GET    | `/ingredients/{id}/stock?at={datetime}&unit={unit}` | Get stock value at a given date | 200 / 400 / 404 |

### Dishes
| Method | URL                        | Description                     | Status          |
|--------|----------------------------|---------------------------------|-----------------|
| GET    | `/dishes`                  | Get all dishes with ingredients | 200             |
| PUT    | `/dishes/{id}/ingredients` | Update dish ingredients         | 200 / 400 / 404 |