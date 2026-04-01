package com.example.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.project.dto.request.EmployeeRequest;
import com.example.project.dto.response.ApiResponse;
import com.example.project.dto.response.EmployeeDTO;
import com.example.project.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(employeeService.findAll()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(employeeService.findById(id)));
    }
    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDTO>> save(@RequestBody EmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(employeeService.save(request)));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateById(@PathVariable Long id, @RequestBody EmployeeRequest request) {
        return ResponseEntity.ok(ApiResponse.success(employeeService.updateById(id, request)));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    
}
