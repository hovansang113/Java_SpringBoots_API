package com.example.project.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.example.project.dto.request.SubjectRequest;
import com.example.project.dto.response.SubjectDTO;
import com.example.project.service.SubjectService;
import com.example.project.repository.SubjectRepository;
import com.example.project.repository.EmployeeRepository;
import com.example.project.repository.SchoolRepository;
import com.example.project.entity.Subject;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.entity.Employee;
import com.example.project.entity.School;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubjectIplmService implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final EmployeeRepository employeeRepository;
    private final SchoolRepository schoolRepository;

    public SubjectIplmService(SubjectRepository subjectRepository, EmployeeRepository employeeRepository,
            SchoolRepository schoolRepository) {
        this.subjectRepository = subjectRepository;
        this.employeeRepository = employeeRepository;
        this.schoolRepository = schoolRepository;
    }

    @Override
    public void deleteById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học id: " + id));
        // Xóa liên kết với employee
        if (subject.getTeachers() != null) {
            subject.getTeachers().forEach(e -> {
                e.setSubject(null);
                employeeRepository.save(e);
            });
        }
        subjectRepository.deleteById(id);
    }

    @Override
    public List<SubjectDTO> findAll() {
        return subjectRepository.findAllWithDetails()
                .stream()
                .map(SubjectDTO::new)
                .toList();
    }

    @Override
    public SubjectDTO findById(Long id) {
        Subject subject = subjectRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học id: " + id));
        return new SubjectDTO(subject);
    }

    @Override
    @Transactional
    public SubjectDTO save(SubjectRequest request) {
        Subject subject = new Subject();
        subject.setName(request.getName());

        if (request.getSchoolId() != null) {
            School school = schoolRepository.findById(request.getSchoolId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Không tìm thấy trường id: " + request.getSchoolId()));
            subject.setSchool(school);
        }

        Subject saved = subjectRepository.save(subject);

        // Gán subject cho từng employee
        if (request.getEmployeeIds() != null) {
            for (Long employeeId : request.getEmployeeIds()) {
                Employee employee = employeeRepository.findById(employeeId)
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên id: " + employeeId));
                employee.setSubject(saved);
                employeeRepository.save(employee);
            }
        }

        return subjectRepository.findWithDetailsById(saved.getId())
                .map(SubjectDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi khi load môn học sau khi save"));
    }

    @Override
    public SubjectDTO updateById(Long id, SubjectRequest request) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học id: " + id));

        subject.setName(request.getName());
        if (request.getSchoolId() != null) {
            School school = schoolRepository.findById(request.getSchoolId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Không tìm thấy trường id: " + request.getSchoolId()));
            subject.setSchool(school);
        } else {
            subject.setSchool(null);
        }

        Subject updated = subjectRepository.save(subject);

        if (request.getEmployeeIds() != null) {
            // Xóa liên kết cũ
            if (subject.getTeachers() != null) {
                subject.getTeachers().forEach(e -> {
                    e.setSubject(null);
                    employeeRepository.save(e);
                });
            }
            // Gán subject cho từng employee mới
            for (Long employeeId : request.getEmployeeIds()) {
                Employee employee = employeeRepository.findById(employeeId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Không tìm thấy nhân viên id: " + employeeId));
                employee.setSubject(updated);
                employeeRepository.save(employee);
            }
        }
        return subjectRepository.findWithDetailsById(updated.getId())
                    .map(SubjectDTO::new)
                    .orElseThrow(() -> new ResourceNotFoundException("Lỗi khi load môn học sau khi update"));
    }

    @Override
    public List<SubjectDTO> findByName(String name) {
        return subjectRepository.findAllByName(name)
                .stream()
                .map(SubjectDTO::new)
                .collect(Collectors.toList());
    }


}
