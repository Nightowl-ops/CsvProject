package com.project.CsvProject.service;

import com.project.CsvProject.model.Employee;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class SalaryCalculatorService {

    private static final double MIN_PROJECT_COMPLETION = 60.0;
    private static final double YEARLY_TENURE_RATE = 0.02;

    public BigDecimal calculateUpdatedSalary(Employee employee) {
        return calculateUpdatedSalary(employee, LocalDate.now());
    }

    // Overloaded method allowing a reference date makes unit testing deterministic
    public BigDecimal calculateUpdatedSalary(Employee employee, LocalDate referenceDate) {
        // Rule 1: Project completion threshold
        if (employee.getProjectCompletionRate() < MIN_PROJECT_COMPLETION) {
            return employee.getCurrentSalary();
        }

        // Rule 2: Base role rate
        double totalRate = employee.getRole().getBaseRate();

        // Rule 3: Tenure rate (only if completed at least 1 full year)
        long yearsWorked = ChronoUnit.YEARS.between(employee.getJoinedDate(), referenceDate);
        if (yearsWorked >= 1) {
            totalRate += (yearsWorked * YEARLY_TENURE_RATE);
        }

        // Updated Salary = Current Salary * (1 + totalRate)
        BigDecimal multiplier = BigDecimal.valueOf(1.0 + totalRate);
        return employee.getCurrentSalary()
                .multiply(multiplier)
                .setScale(2, RoundingMode.HALF_UP);
    }
}