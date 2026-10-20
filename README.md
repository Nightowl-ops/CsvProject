# Concurrent CSV Data Processor (Spring Boot)

An enterprise-style data processing application engineered to read employee dataset records, calculate performance- and tenure-based salary increments concurrently, and expose the aggregated results via a REST API.

## Architecture & Design Patterns
- **Layered Clean Architecture**: Separation of concerns across Controller, Orchestrator, Concurrency Engine, and Business Rules.
- **Thread Safety**: Coordinated using `ExecutorService`, `ReentrantLock`, `Semaphore`, and non-blocking Atomics.
- **RESTful Output**: Dynamic JSON response output designed for inspection and API testing via Postman.

## Business Logic & Salary Rules

Salary adjustments are computed deterministically based on employee role, evaluation score, and company tenure:
1. **Performance Threshold**: Employees with a performance evaluation score below `60.0%` receive an immediate `0.0%` raise, bypassing tenure and role increments.
2. **Role Base Rates**:
    - `DIRECTOR`: 5% base increment
    - `MANAGER`: 2% base increment
    - `EMPLOYEE`: 1% base increment
3. **Tenure Accumulation**: Eligible employees receive an additional `+2%` for each full year of service (minimum 1 full year required).
4. **Financial Precision**: All calculations utilize Java `BigDecimal` with half-up rounding to prevent floating-point drift.

## Concurrency & Thread Synchronization

The engine uses Java's `java.util.concurrent` primitives to guarantee thread safety:
- **`ExecutorService (FixedThreadPool)`**: Dispatches individual employee records across parallel worker threads.
- **`Semaphore(2)`**: Acts as a throttling mechanism, limiting concurrent execution blocks to a maximum of 2 simultaneous threads.
- **`ReentrantLock`**: Mutually excludes threads when modifying shared memory collections (`processedList.add(emp)`), preventing race conditions and index corruption.
- **`AtomicInteger`**: Tracks records processed using hardware-level Compare-And-Swap (CAS) instructions.
- **`DoubleAdder`**: High-throughput thread-safe accumulator tracking aggregate payroll expenditure across threads without lock contention.
