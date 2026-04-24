# SochUPI 💸

A behavioral fintech app that helps users become aware of their spending habits.

## Tech Stack

| Layer    | Technology                    |
|----------|-------------------------------|
| Frontend | React Native (Expo + TypeScript) |
| Backend  | Java Spring Boot 3.2          |
| Database | MySQL                         |

## Project Structure

```
SochUPI/
├── backend/     → Spring Boot REST API
└── frontend/    → React Native (Expo) app
```

## Getting Started

### Prerequisites
- Java 17+
- Node.js 18+
- MySQL 8+

### Backend
```bash
cd backend
# Create the database (or let Spring auto-create it)
mvn spring-boot:run
```
Runs on `http://localhost:8080`

### Frontend
```bash
cd frontend
npm install
npx expo start
```

## Status
🚧 **Phase 1** — Basic project setup. Entities, APIs, and screens coming next.
