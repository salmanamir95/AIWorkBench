| Layer      | Responsibilities                         | Example Classes           |
| ---------- | ---------------------------------------- | ------------------------- |
| Repository | Access DB, CRUD operations               | `UserAuthRepository`      |
| Service    | Business logic, transactions, validation | `UserAuthService`         |
| Controller | Handle HTTP requests, return responses   | `AuthController`          |


Controller → Service → Repository → DB



| Component             | Responsibility                                         |
| --------------------- | ------------------------------------------------------ |
| `AuthenticationProvider` | Fetch user credentials + validate password          |
| `AuthenticationVerifier` | Verify account state (locked, email verified, etc.) |
| `AuthenticationManager`  | Orchestrates authentication flow                    |
