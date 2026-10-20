package com.project.CsvProject;

import com.project.CsvProject.model.Employee;
import com.project.CsvProject.model.Role;
import com.project.CsvProject.service.CsvReaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvReaderServiceTest {

    private CsvReaderService csvReaderService;

    @BeforeEach
    void setUp() {
        csvReaderService = new CsvReaderService();
    }

    @Test
    void shouldReadAndParseAllEmployeesFromCsv() throws IOException {
        List<Employee> employees = csvReaderService.readEmployeesFromCsv("employees.csv");

        assertNotNull(employees, "The parsed employee list should not be null");
        assertEquals(4, employees.size(), "Should have parsed exactly 4 records from employees.csv");

        Employee alice = employees.get(0);
        assertEquals("Alice Smith", alice.getName());
        assertEquals(new BigDecimal("50000.00"), alice.getCurrentSalary());
        assertEquals(LocalDate.of(2021, 3, 15), alice.getJoinedDate());
        assertEquals(Role.EMPLOYEE, alice.getRole());
        assertEquals(75.0, alice.getProjectCompletionRate());
        assertNull(alice.getUpdatedSalary(), "Updated salary should initially be null before calculation");

        Employee bob = employees.get(1);
        assertEquals("Bob Johnson", bob.getName());
        assertEquals(Role.MANAGER, bob.getRole());
        assertEquals(55.0, bob.getProjectCompletionRate());
    }

    @Test
    void shouldThrowExceptionWhenFileNotFound() {
        assertThrows(IOException.class, () -> {
            csvReaderService.readEmployeesFromCsv("non_existent_file.csv");
        });
    }
}