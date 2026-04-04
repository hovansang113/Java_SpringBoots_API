package com.example.project.service;

import com.example.project.dto.request.AcademicRecordRequest;
import com.example.project.dto.response.AcademicRecordDTO;
import java.util.List;

public interface AcademicRecordService {
    List<AcademicRecordDTO> findAll();
    AcademicRecordDTO findById(Long id);
    List<AcademicRecordDTO> findByStudentId(Long studentId);
    List<AcademicRecordDTO> findByStudentName(String studentName);
    List<AcademicRecordDTO> findBySchoolYearAndSemester(String schoolYear, String semester);
    AcademicRecordDTO save(AcademicRecordRequest request);
    AcademicRecordDTO updateById(Long id, AcademicRecordRequest request);
    void deleteById(Long id);
}
