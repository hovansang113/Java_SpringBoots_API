package com.example.project.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.project.service.ReportCardService;
import com.example.project.dto.request.ReportCardRequest;
import com.example.project.dto.response.ApiResponse;
import com.example.project.dto.response.ReportCardDTO;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
@RestController
@RequestMapping("/api/report-cards")
public class ReportCardController {
    private final ReportCardService reportCardService;

    public ReportCardController(ReportCardService reportCardService) {
        this.reportCardService = reportCardService;
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReportCardDTO>>> getAll(){
        return ResponseEntity.ok(ApiResponse.success(reportCardService.findAll()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReportCardDTO>> getById(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(reportCardService.findById(id)));
    }
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<ReportCardDTO>>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(reportCardService.findByStudentId(studentId)));
    }

    @GetMapping("/search-by-student-name")
    public ResponseEntity<ApiResponse<List<ReportCardDTO>>> getByStudentName(@RequestParam String studentName){
        return ResponseEntity.ok(ApiResponse.success(reportCardService.findByStudentName(studentName)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReportCardDTO>> updateById(@PathVariable Long id, @RequestBody ReportCardRequest request) {
    return ResponseEntity.ok(ApiResponse.success(reportCardService.updateById(id, request)));
}

    
}
