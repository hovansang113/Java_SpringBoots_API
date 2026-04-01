package com.example.project.service.impl;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project.dto.request.SchoolRequest;
import com.example.project.dto.response.SchoolDTO;
import com.example.project.repository.SchoolRepository;
import com.example.project.service.SchoolService;
import com.example.project.entity.School;
import com.example.project.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class SchoolIplmService implements SchoolService{
    private final SchoolRepository schoolRepository;

    public SchoolIplmService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }
    @Override
    @Transactional
    public void deleteById(Long id) {
        schoolRepository.deleteById(id);
    }
    @Override
    public List<SchoolDTO> findAll() {
        return schoolRepository.findAll().stream().map(school -> new SchoolDTO(school.getId(), school.getName(), school.getAddress(), school.getPhone(), school.getEmail(), school.getLevel())).toList();
    }
    @Override
    public SchoolDTO findById(Long id) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy trường id: " + id));
        return new SchoolDTO(school.getId(), school.getName(), school.getAddress(), school.getPhone(), school.getEmail(), school.getLevel());
    }
    
    @Override
    public SchoolDTO save(SchoolRequest request) {
        School school = new School();
        school.setName(request.getName());
        school.setAddress(request.getAddress());
        school.setPhone(request.getPhone());
        school.setEmail(request.getEmail());
        school.setLevel(request.getLevel());
        School saved = schoolRepository.save(school);
        return new SchoolDTO(saved.getId(), saved.getName(), saved.getAddress(), saved.getPhone(), saved.getEmail(), saved.getLevel());
    }
    @Override
    public SchoolDTO updateById(Long id, SchoolRequest request) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy trường id: " + id));
        school.setName(request.getName());
        school.setAddress(request.getAddress());
        school.setPhone(request.getPhone());
        school.setEmail(request.getEmail());
        school.setLevel(request.getLevel());
        School updated = schoolRepository.save(school);
        return new SchoolDTO(updated.getId(), updated.getName(), updated.getAddress(), updated.getPhone(), updated.getEmail(), updated.getLevel());
    }


}
