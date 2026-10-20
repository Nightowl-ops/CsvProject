package com.project.CsvProject.service;

import com.project.CsvProject.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ConcurrentProcessorService {

    private static final Logger log = LoggerFactory.getLogger(ConcurrentProcessorService.class);

    private final SalaryCalculatorService salaryCalculatorService;

    public ConcurrentProcessorService(SalaryCalculatorService salaryCalculatorService) {
        this.salaryCalculatorService = salaryCalculatorService;
    }

    // the lock to be used
    private final ReentrantLock lock = new ReentrantLock();
    // the number of threads to be used fixed if both are used we will wait the next operation

     private final Semaphore semaphore = new Semaphore(2);

// to avoid race condition count the number of employees scanned

    private final AtomicInteger processedCount = new AtomicInteger(0);

    //track the total dollar amount the company is adding to its payroll across all employees
    private final DoubleAdder totalIncrementPool = new DoubleAdder();
    public List<Employee> processEmployeesConcurrently(List<Employee> rawEmployees, int threadPoolSize)
            throws InterruptedException {

        // 1. Reset counters for a fresh run
        processedCount.set(0);
        totalIncrementPool.reset();

        List<Employee> processedList = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        log.info("Starting concurrent processing of {} records using {} threads",
                rawEmployees.size(), threadPoolSize);

        try {
            List<Callable<Void>> tasks = new ArrayList<>();

            for (Employee emp : rawEmployees) {
                tasks.add(() -> {
                    // 1. Acquire permit from Semaphore (limits concurrent processing to 2 threads)
                    semaphore.acquire();
                    try {
                        log.info("[Thread {}] Processing salary for {}",
                                Thread.currentThread().getName(), emp.getName());

                        // 2. Calculate the updated salary using the business rules service
                        BigDecimal updatedSalary = salaryCalculatorService.calculateUpdatedSalary(emp);
                        emp.setUpdatedSalary(updatedSalary);

                        // 3. Atomically update metrics without locking overhead
                        BigDecimal diff = updatedSalary.subtract(emp.getCurrentSalary());
                        totalIncrementPool.add(diff.doubleValue());
                        processedCount.incrementAndGet();

                        // 4. ReentrantLock guards the shared ArrayList write
                        lock.lock();
                        try {
                            processedList.add(emp);
                        } finally {
                            lock.unlock();
                        }

                    } finally {
                        // 5. Release permit back so waiting threads can proceed
                        semaphore.release();
                    }
                    return null;
                });
            }

            // Submit all tasks and block until every thread finishes
            List<Future<Void>> futures = executor.invokeAll(tasks);
            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (ExecutionException e) {
                    log.error("Error executing task: ", e.getCause());
                }
            }

        } finally {
            // Graceful shutdown of the thread pool
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        }

        log.info("Finished processing {} records. Total company payroll increase: ${}",
                processedCount.get(), totalIncrementPool.doubleValue());

        return processedList;
    }


}