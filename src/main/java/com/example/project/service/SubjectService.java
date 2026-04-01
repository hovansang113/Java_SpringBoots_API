package com.example.project.service;

import java.util.List;
import com.example.project.dto.request.SubjectRequest;
import com.example.project.dto.response.SubjectDTO;

public interface SubjectService {
    void deleteById(Long id);
    SubjectDTO findById(Long id);
    SubjectDTO save(SubjectRequest request);
    SubjectDTO updateById(Long id, SubjectRequest request);
    List<SubjectDTO> findAll();
    List<SubjectDTO> findByName(String name);
}
