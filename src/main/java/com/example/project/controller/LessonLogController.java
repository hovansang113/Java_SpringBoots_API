package com.example.project.controller;

import com.example.project.dto.request.LessonLogRequest;
import com.example.project.dto.response.ApiResponse;
import com.example.project.dto.response.LessonLogDTO;
import com.example.project.service.LessonLogService;
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
@RequestMapping("/api/lesson-logs")
public class LessonLogController {

    private final LessonLogService lessonLogService;

    public LessonLogController(LessonLogService lessonLogService) {
        this.lessonLogService = lessonLogService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LessonLogDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(lessonLogService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LessonLogDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(lessonLogService.findById(id)));
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<ApiResponse<List<LessonLogDTO>>> getByClassId(@PathVariable Long classId) {
        return ResponseEntity.ok(ApiResponse.success(lessonLogService.findByClassId(classId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LessonLogDTO>> save(@RequestBody LessonLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(lessonLogService.save(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LessonLogDTO>> updateById(@PathVariable Long id,
            @RequestBody LessonLogRequest request) {
        return ResponseEntity.ok(ApiResponse.success(lessonLogService.updateById(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable Long id) {
        lessonLogService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
