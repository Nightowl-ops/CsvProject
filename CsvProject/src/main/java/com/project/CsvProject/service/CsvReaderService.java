package com.project.CsvProject.service;

import com.project.CsvProject.model.Employee;
import com.project.CsvProject.model.Role;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvReaderService {

    public List<Employee> readEmployeesFromCsv(String resourceFileName) throws IOException {
        List<Employee> employees = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(resourceFileName);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                // Ignore empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Skip the top CSV header row
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                Employee employee = parseLineToEmployee(line);
                employees.add(employee);
            }
        }

        return employees;
    }

    private Employee parseLineToEmployee(String line) {
        String[] tokens = line.split(",");

        String name = tokens[0].trim();
        BigDecimal currentSalary = new BigDecimal(tokens[1].trim());
        LocalDate joinedDate = LocalDate.parse(tokens[2].trim());
        Role role = Role.fromString(tokens[3]);
        double projectCompletionRate = Double.parseDouble(tokens[4].trim());

        return new Employee(name, currentSalary, joinedDate, role, projectCompletionRate, null);
    }
}