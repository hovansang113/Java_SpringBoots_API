package com.example.project.service;

import com.example.project.dto.request.StudentRequest;
import com.example.project.dto.response.StudentDTO;
import java.util.List;

public interface StudentService {
    List<StudentDTO> findAll();
    StudentDTO save(StudentRequest request);
    StudentDTO findById(Long id);
    StudentDTO updateById(Long id, StudentRequest request);
    void deleteById(Long id);
}
