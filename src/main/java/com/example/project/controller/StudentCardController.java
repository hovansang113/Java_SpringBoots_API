package com.example.project.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.project.dto.request.StudentCardRequest;
import com.example.project.dto.response.ApiResponse;
import com.example.project.dto.response.StudentCardDTO;
import com.example.project.service.StudentCardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;
@RestController
@RequestMapping("/api/student-cards")
public class StudentCardController {
    
    private final StudentCardService studentCardService;

    public StudentCardController(StudentCardService studentCardService) {
        this.studentCardService = studentCardService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentCardDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(studentCardService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentCardDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(studentCardService.findById(id)));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<StudentCardDTO>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(studentCardService.findByStudentId(studentId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentCardDTO>> save(@RequestBody StudentCardRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(studentCardService.save(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentCardDTO>> updateById(@PathVariable Long id, @RequestBody StudentCardRequest request) {
        return ResponseEntity.ok(ApiResponse.success(studentCardService.updateById(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable Long id) {
        studentCardService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
