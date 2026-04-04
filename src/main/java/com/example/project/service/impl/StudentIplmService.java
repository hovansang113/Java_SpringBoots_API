package com.example.project.service.impl;

import com.example.project.dto.request.StudentRequest;
import com.example.project.dto.response.StudentDTO;
import com.example.project.entity.ClassEntity;
import com.example.project.entity.Parent;
import com.example.project.entity.ReportCard;
import com.example.project.entity.Student;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.repository.ClassEntityRepository;
import com.example.project.repository.ParentRepository;
import com.example.project.repository.ReportCardRepository;
import com.example.project.repository.StudentRepository;
import com.example.project.repository.SubjectRepository;
import com.example.project.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentIplmService implements StudentService {

    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final ClassEntityRepository classEntityRepository;
    private final SubjectRepository subjectRepository;
    private final ReportCardRepository reportCardRepository;

    public StudentIplmService(StudentRepository studentRepository,
                               ParentRepository parentRepository,
                               ClassEntityRepository classEntityRepository,
                               SubjectRepository subjectRepository,
                               ReportCardRepository reportCardRepository) {
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.classEntityRepository = classEntityRepository;
        this.subjectRepository = subjectRepository;
        this.reportCardRepository = reportCardRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> findAll() {
        return studentRepository.findAllWithDetails()
                .stream()
                .map(StudentDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StudentDTO save(StudentRequest request) {
        Student student = new Student();
        student.setFullName(request.getFullName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setGender(request.getGender());
        student.setAddress(request.getAddress());

        if (request.getParentId() != null) {
            Parent parent = parentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phụ huynh id: " + request.getParentId()));
            student.setParent(parent);
        }

        if (request.getClassId() != null) {
            ClassEntity classEntity = classEntityRepository.findById(request.getClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp id: " + request.getClassId()));
            student.setClassEntity(classEntity);
        }

        Student saved = studentRepository.save(student);

        // Tự động tạo ReportCard cho tất cả môn học
        if (request.getSchoolYear() != null && request.getSemester() != null) {
            subjectRepository.findAll().forEach(subject -> {
                ReportCard reportCard = new ReportCard();
                reportCard.setStudent(saved);
                reportCard.setSubject(subject);
                reportCard.setSchoolYear(request.getSchoolYear());
                reportCard.setSemester(request.getSemester());
                reportCard.setScore(null);
                reportCardRepository.save(reportCard);
            });
        }

        return studentRepository.findWithDetailsById(saved.getId())
                .map(StudentDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi khi load student sau khi save"));
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO findById(Long id) {
        return studentRepository.findWithDetailsById(id)
                .map(StudentDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy học sinh id: " + id));
    }

    @Override
    @Transactional
    public StudentDTO updateById(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy học sinh id: " + id));

        student.setFullName(request.getFullName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setGender(request.getGender());
        student.setAddress(request.getAddress());

        if (request.getParentId() != null) {
            Parent parent = parentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phụ huynh id: " + request.getParentId()));
            student.setParent(parent);
        } else {
            student.setParent(null);
        }

        if (request.getClassId() != null) {
            ClassEntity classEntity = classEntityRepository.findById(request.getClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp id: " + request.getClassId()));
            student.setClassEntity(classEntity);
        } else {
            student.setClassEntity(null);
        }

        Student updated = studentRepository.save(student);
        return studentRepository.findWithDetailsById(updated.getId())
                .map(StudentDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi khi load student sau khi update"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }
}
