# SpinachAI 🌿

A smart Q&A bot that answers user questions, built with Spring Boot.

## Tech Stack
- Java + Spring Boot (Backend)
- H2 In-Memory Database
- Maven (Build tool)
- Git & GitHub (Version Control)
- Postman (API Testing)

## How to Run
1. Clone the repository
```
   git clone https://github.com/TAsamidinov/spinachai.git
```
2. Navigate into the project
```
   cd spinachai
```
3. Run the application
```
   mvn spring-boot:run
```
4. App runs at `http://localhost:8080`

## Database Console
H2 console available at `http://localhost:8080/h2-console` during development.

## API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | /hello   | Health check |

## Testing
API endpoints are tested using Postman.