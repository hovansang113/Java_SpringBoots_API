package com.example.project.controller;

import com.example.project.dto.request.ParentRequest;
import com.example.project.dto.response.ApiResponse;
import com.example.project.dto.response.ParentDTO;
import com.example.project.service.ParentService;
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
@RequestMapping("/api/parents")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ParentDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(parentService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ParentDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(parentService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ParentDTO>> save(@Valid @RequestBody ParentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(parentService.save(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ParentDTO>> updateById(@PathVariable Long id,
            @Valid @RequestBody ParentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(parentService.updateById(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable Long id) {
        parentService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
