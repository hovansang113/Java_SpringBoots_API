package com.example.project.service;

import com.example.project.dto.request.SchoolRequest;
import com.example.project.dto.response.SchoolDTO;
import java.util.List;

public interface SchoolService {
    List<SchoolDTO> findAll();
    SchoolDTO findById(Long id);
    SchoolDTO save(SchoolRequest request);
    SchoolDTO updateById(Long id, SchoolRequest request);
    void deleteById(Long id);
}
