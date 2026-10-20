package com.project.CsvProject.controller;

import com.project.CsvProject.dto.EmployeeProcessingResponse;
import com.project.CsvProject.service.EmployeeOrchestratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeOrchestratorService orchestratorService;

    public EmployeeController(EmployeeOrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    @GetMapping("/process")
    public ResponseEntity<EmployeeProcessingResponse> processEmployees(
            @RequestParam(defaultValue = "4") int threads) throws IOException, InterruptedException {

        EmployeeProcessingResponse response = orchestratorService.executeProcessingPipeline(threads);
        return ResponseEntity.ok(response);
    }
}