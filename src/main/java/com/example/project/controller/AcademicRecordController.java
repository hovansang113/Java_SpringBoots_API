package com.example.project.controller;

import com.example.project.dto.request.AcademicRecordRequest;
import com.example.project.dto.response.AcademicRecordDTO;
import com.example.project.dto.response.ApiResponse;
import com.example.project.service.AcademicRecordService;
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
@RequestMapping("/api/academic-records")
public class AcademicRecordController {

    private final AcademicRecordService academicRecordService;

    public AcademicRecordController(AcademicRecordService academicRecordService) {
        this.academicRecordService = academicRecordService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AcademicRecordDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(academicRecordService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AcademicRecordDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(academicRecordService.findById(id)));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<AcademicRecordDTO>>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(academicRecordService.findByStudentId(studentId)));
    }

    @GetMapping("/search/student-name")
    public ResponseEntity<ApiResponse<List<AcademicRecordDTO>>> getByStudentName(@RequestParam String name) {
        return ResponseEntity.ok(ApiResponse.success(academicRecordService.findByStudentName(name)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AcademicRecordDTO>>> getBySchoolYearAndSemester(
            @RequestParam String schoolYear,
            @RequestParam String semester) {
        return ResponseEntity.ok(ApiResponse.success(
                academicRecordService.findBySchoolYearAndSemester(schoolYear, semester)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AcademicRecordDTO>> save(@RequestBody AcademicRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(academicRecordService.save(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AcademicRecordDTO>> updateById(@PathVariable Long id,
            @RequestBody AcademicRecordRequest request) {
        return ResponseEntity.ok(ApiResponse.success(academicRecordService.updateById(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable Long id) {
        academicRecordService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
