package com.example.project.controller;

import com.example.project.dto.request.SchoolRequest;
import com.example.project.dto.response.ApiResponse;
import com.example.project.dto.response.SchoolDTO;
import com.example.project.service.SchoolService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/schools")
public class SchoolController {

    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SchoolDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(schoolService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SchoolDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(schoolService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SchoolDTO>> save(@Valid @RequestBody SchoolRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(schoolService.save(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SchoolDTO>> updateById(@PathVariable Long id,
            @Valid @RequestBody SchoolRequest request) {
        return ResponseEntity.ok(ApiResponse.success(schoolService.updateById(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable Long id) {
        schoolService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
