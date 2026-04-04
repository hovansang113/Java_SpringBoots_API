package com.example.project.service;

import com.example.project.dto.request.ScoreDetailRequest;
import com.example.project.dto.response.ScoreDetailDTO;
import java.util.List;

public interface ScoreDetailService {
    List<ScoreDetailDTO> findAll();
    ScoreDetailDTO findById(Long id);
    List<ScoreDetailDTO> findByStudentId(Long studentId);
    List<ScoreDetailDTO> findByStudentIdAndSemester(Long studentId, String schoolYear, String semester);
    ScoreDetailDTO save(ScoreDetailRequest request);
    ScoreDetailDTO updateById(Long id, ScoreDetailRequest request);
    void deleteById(Long id);
}
