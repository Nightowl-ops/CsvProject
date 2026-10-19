package com.project.CsvProject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private String name;
    private BigDecimal currentSalary;
    private LocalDate joinedDate;
    private Role role;
    private double projectCompletionRate;
    private BigDecimal updatedSalary;
}