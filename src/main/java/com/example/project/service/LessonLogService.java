package com.example.project.service;
import com.example.project.dto.request.LessonLogRequest;
import com.example.project.dto.response.LessonLogDTO;
import java.util.List;

public interface LessonLogService {
    List<LessonLogDTO> findAll();
    LessonLogDTO findById(Long id);
    List<LessonLogDTO> findByClassId(Long classId);
    LessonLogDTO save(LessonLogRequest request);
    LessonLogDTO updateById(Long id, LessonLogRequest request);
    void deleteById(Long id);
}
