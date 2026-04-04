package com.example.project.service;

import com.example.project.dto.request.ReportCardRequest;
import com.example.project.dto.response.ReportCardDTO;
import java.util.List;

public interface ReportCardService {
    List<ReportCardDTO> findAll();
    ReportCardDTO findById(Long id);
    List<ReportCardDTO> findByStudentId(Long studentId);
    List<ReportCardDTO> findByStudentName(String studentName);
    ReportCardDTO updateById(Long id, ReportCardRequest request);
    void deleteById(Long id);
}
