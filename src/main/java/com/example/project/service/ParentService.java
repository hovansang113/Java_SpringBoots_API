package com.example.project.service;

import java.util.List;
import com.example.project.dto.response.ParentDTO;
import com.example.project.dto.request.ParentRequest;

public interface ParentService {
    List<ParentDTO> findAll();
    ParentDTO findById(Long id);
    ParentDTO save(ParentRequest request);
    ParentDTO updateById(Long id, ParentRequest request);
    void deleteById(Long id);
}
