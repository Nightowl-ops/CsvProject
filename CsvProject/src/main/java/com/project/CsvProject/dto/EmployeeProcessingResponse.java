package com.project.CsvProject.dto;

import com.project.CsvProject.model.Employee;
import java.util.List;

public class EmployeeProcessingResponse {
    private String status;
    private int threadPoolSize;
    private int recordsProcessed;
    private double totalPayrollIncrease;
    private long executionTimeMs;
    private List<Employee> employees;

    public EmployeeProcessingResponse(String status, int threadPoolSize, int recordsProcessed,
                                      double totalPayrollIncrease, long executionTimeMs,
                                      List<Employee> employees) {
        this.status = status;
        this.threadPoolSize = threadPoolSize;
        this.recordsProcessed = recordsProcessed;
        this.totalPayrollIncrease = totalPayrollIncrease;
        this.executionTimeMs = executionTimeMs;
        this.employees = employees;
    }

    public String getStatus() { return status; }
    public int getThreadPoolSize() { return threadPoolSize; }
    public int getRecordsProcessed() { return recordsProcessed; }
    public double getTotalPayrollIncrease() { return totalPayrollIncrease; }
    public long getExecutionTimeMs() { return executionTimeMs; }
    public List<Employee> getEmployees() { return employees; }
}