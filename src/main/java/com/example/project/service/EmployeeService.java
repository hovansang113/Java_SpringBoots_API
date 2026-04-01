package com.example.project.service;

import java.util.List;
import com.example.project.dto.request.EmployeeRequest;
import com.example.project.dto.response.EmployeeDTO;

public interface EmployeeService {
    List<EmployeeDTO> findAll();
    EmployeeDTO findById(Long id);
    EmployeeDTO save(EmployeeRequest request);
    EmployeeDTO updateById(Long id, EmployeeRequest request);
    void deleteById(Long id);
}
