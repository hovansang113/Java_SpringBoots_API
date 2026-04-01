package com.example.project.service.impl;

import com.example.project.dto.request.EmployeeRequest;
import com.example.project.dto.response.EmployeeDTO;
import com.example.project.entity.Employee;
import com.example.project.entity.School;
import com.example.project.entity.Subject;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.repository.EmployeeRepository;
import com.example.project.repository.SchoolRepository;
import com.example.project.repository.SubjectRepository;
import com.example.project.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeIplmService implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final SchoolRepository schoolRepository;
    private final SubjectRepository subjectRepository;

    public EmployeeIplmService(EmployeeRepository employeeRepository,
                                SchoolRepository schoolRepository,
                                SubjectRepository subjectRepository) {
        this.employeeRepository = employeeRepository;
        this.schoolRepository = schoolRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> findAll() {
        return employeeRepository.findAllWithDetails()
                .stream()
                .map(EmployeeDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO findById(Long id) {
        return employeeRepository.findWithDetailsById(id)
                .map(EmployeeDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên id: " + id));
    }

    @Override
    @Transactional
    public EmployeeDTO save(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setFullName(request.getFullName());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setGender(request.getGender());
        employee.setPhone(request.getPhone());
        employee.setEmail(request.getEmail());
        employee.setPosition(request.getPosition());

        if (request.getSchoolId() != null) {
            School school = schoolRepository.findById(request.getSchoolId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy trường id: " + request.getSchoolId()));
            employee.setSchool(school);
        }

        // subject_id nằm trong bảng employee nên set trực tiếp
        if (request.getSubjectId() != null) {
            Subject subject = subjectRepository.findById(request.getSubjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học id: " + request.getSubjectId()));
            employee.setSubject(subject);
        }

        Employee saved = employeeRepository.save(employee);
        return employeeRepository.findWithDetailsById(saved.getId())
                .map(EmployeeDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi khi load nhân viên sau khi save"));
    }

    @Override
    @Transactional
    public EmployeeDTO updateById(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên id: " + id));

        employee.setFullName(request.getFullName());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setGender(request.getGender());
        employee.setPhone(request.getPhone());
        employee.setEmail(request.getEmail());
        employee.setPosition(request.getPosition());

        if (request.getSchoolId() != null) {
            School school = schoolRepository.findById(request.getSchoolId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy trường id: " + request.getSchoolId()));
            employee.setSchool(school);
        } else {
            employee.setSchool(null);
        }

        if (request.getSubjectId() != null) {
            Subject subject = subjectRepository.findById(request.getSubjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học id: " + request.getSubjectId()));
            employee.setSubject(subject);
        } else {
            employee.setSubject(null);
        }

        Employee updated = employeeRepository.save(employee);
        return employeeRepository.findWithDetailsById(updated.getId())
                .map(EmployeeDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi khi load nhân viên sau khi update"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên id: " + id));
        employeeRepository.deleteById(id);
    }
}
