package com.example.project.service;

import com.example.project.dto.request.StudentCardRequest;
import com.example.project.dto.response.StudentCardDTO;
import java.util.List;

public interface StudentCardService {
    List<StudentCardDTO> findAll();
    StudentCardDTO findById(Long id);
    StudentCardDTO findByStudentId(Long studentId);
    StudentCardDTO save(StudentCardRequest request);
    StudentCardDTO updateById(Long id, StudentCardRequest request);
    void deleteById(Long id);
}
