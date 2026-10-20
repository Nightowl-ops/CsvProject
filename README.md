# Concurrent CSV Data Processor (Spring Boot)

An enterprise-style data processing application engineered to read employee dataset records, calculate performance- and tenure-based salary increments concurrently, and expose the aggregated results via a REST API.

## Architecture & Design Patterns
- **Layered Clean Architecture**: Separation of concerns across Controller, Orchestrator, Concurrency Engine, and Business Rules.
- **Thread Safety**: Coordinated using `ExecutorService`, `ReentrantLock`, `Semaphore`, and non-blocking Atomics.
- **RESTful Output**: Dynamic JSON response output designed for inspection and API testing via Postman.