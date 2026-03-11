| Layer      | Responsibilities                         | Example Classes  |
| ---------- | ---------------------------------------- | ---------------- |
| Repository | Access DB, CRUD operations               | `UserRepository` |
| Service    | Business logic, transactions, validation | `UserService`    |
| Controller | Handle HTTP requests, return responses   | `UserController` |


Controller → Service → Repository → DB



| Component    | Responsibility                                  |
| ------------ | ----------------------------------------------- |
| AuthProvider | Fetch user credentials from a source            |
| AuthVerifier | Verify credentials (password, token, OTP, etc.) |
| AuthManager  | Orchestrates authentication logic               |
