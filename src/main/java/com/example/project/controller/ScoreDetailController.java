package com.example.project.controller;

import com.example.project.dto.request.ScoreDetailRequest;
import com.example.project.dto.response.ApiResponse;
import com.example.project.dto.response.ScoreDetailDTO;
import com.example.project.service.ScoreDetailService;
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
@RequestMapping("/api/score-details")
public class ScoreDetailController {

    private final ScoreDetailService scoreDetailService;

    public ScoreDetailController(ScoreDetailService scoreDetailService) {
        this.scoreDetailService = scoreDetailService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ScoreDetailDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(scoreDetailService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ScoreDetailDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(scoreDetailService.findById(id)));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<ScoreDetailDTO>>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(scoreDetailService.findByStudentId(studentId)));
    }

    @GetMapping("/student/{studentId}/semester")
    public ResponseEntity<ApiResponse<List<ScoreDetailDTO>>> getByStudentIdAndSemester(
            @PathVariable Long studentId,
            @RequestParam String schoolYear,
            @RequestParam String semester) {
        return ResponseEntity.ok(ApiResponse.success(
                scoreDetailService.findByStudentIdAndSemester(studentId, schoolYear, semester)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ScoreDetailDTO>> save(@RequestBody ScoreDetailRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(scoreDetailService.save(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ScoreDetailDTO>> updateById(@PathVariable Long id,
            @RequestBody ScoreDetailRequest request) {
        return ResponseEntity.ok(ApiResponse.success(scoreDetailService.updateById(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable Long id) {
        scoreDetailService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
