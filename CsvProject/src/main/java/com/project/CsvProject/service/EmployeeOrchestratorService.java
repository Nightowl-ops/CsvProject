package com.project.CsvProject.service;

import com.project.CsvProject.dto.EmployeeProcessingResponse;
import com.project.CsvProject.model.Employee;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EmployeeOrchestratorService {

    private final CsvReaderService csvReaderService;
    private final ConcurrentProcessorService concurrentProcessorService;

    public EmployeeOrchestratorService(CsvReaderService csvReaderService,
                                       ConcurrentProcessorService concurrentProcessorService) {
        this.csvReaderService = csvReaderService;
        this.concurrentProcessorService = concurrentProcessorService;
    }

    public EmployeeProcessingResponse executeProcessingPipeline(int threadPoolSize)
            throws IOException, InterruptedException {

        long startTime = System.currentTimeMillis();

        // 1. Read records
        List<Employee> rawEmployees = csvReaderService.readEmployeesFromCsv("employees.csv");

        // 2. Process concurrently
        List<Employee> processedEmployees =
                concurrentProcessorService.processEmployeesConcurrently(rawEmployees, threadPoolSize);

        long executionTimeMs = System.currentTimeMillis() - startTime;

        // 3. Package into clean DTO
        return new EmployeeProcessingResponse(
                "SUCCESS",
                threadPoolSize,
                concurrentProcessorService.getProcessedCount(),
                concurrentProcessorService.getTotalIncrementPool(),
                executionTimeMs,
                processedEmployees
        );
    }
}