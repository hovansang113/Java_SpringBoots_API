package com.example.project.controller;

import com.example.project.dto.request.SubjectRequest;
import com.example.project.dto.response.ApiResponse;
import com.example.project.dto.response.SubjectDTO;
import com.example.project.service.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SubjectDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(subjectService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(subjectService.findById(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<SubjectDTO>>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(ApiResponse.success(subjectService.findByName(name)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SubjectDTO>> save(@RequestBody SubjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(subjectService.save(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectDTO>> updateById(@PathVariable Long id, @RequestBody SubjectRequest request) {
        return ResponseEntity.ok(ApiResponse.success(subjectService.updateById(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable Long id) {
        subjectService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
