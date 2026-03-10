| Layer      | Responsibilities                         | Example Classes  |
| ---------- | ---------------------------------------- | ---------------- |
| Repository | Access DB, CRUD operations               | `UserRepository` |
| Service    | Business logic, transactions, validation | `UserService`    |
| Controller | Handle HTTP requests, return responses   | `UserController` |


Controller → Service → Repository → DB