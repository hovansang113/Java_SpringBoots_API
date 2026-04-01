package com.example.project.controller;

import com.example.project.dto.request.StudentRequest;
import com.example.project.dto.response.ApiResponse;
import com.example.project.dto.response.StudentDTO;
import com.example.project.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(studentService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(studentService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentDTO>> save(@RequestBody StudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(studentService.save(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDTO>> updateById(@PathVariable Long id, @RequestBody StudentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(studentService.updateById(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable Long id) {
        studentService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
