package com.example.project.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.dto.request.LessonLogRequest;
import com.example.project.dto.response.LessonLogDTO;
import com.example.project.entity.ClassEntity;
import com.example.project.entity.Employee;
import com.example.project.entity.LessonLog;
import com.example.project.entity.Subject;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.repository.ClassEntityRepository;
import com.example.project.repository.EmployeeRepository;
import com.example.project.repository.LessonLogRepository;
import com.example.project.repository.SubjectRepository;
import com.example.project.service.LessonLogService;

@Service
public class LessonLogIplmService implements LessonLogService {

    private final LessonLogRepository lessonLogRepository;
    private final ClassEntityRepository classEntityRepository;
    private final EmployeeRepository employeeRepository;
    private final SubjectRepository subjectRepository;

    public LessonLogIplmService(LessonLogRepository lessonLogRepository,
                                 ClassEntityRepository classEntityRepository,
                                 EmployeeRepository employeeRepository,
                                 SubjectRepository subjectRepository) {
        this.lessonLogRepository = lessonLogRepository;
        this.classEntityRepository = classEntityRepository;
        this.employeeRepository = employeeRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonLogDTO> findAll() {
        return lessonLogRepository.findAllWithDetails()
                .stream()
                .map(LessonLogDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LessonLogDTO findById(Long id) {
        return lessonLogRepository.findWithDetailsById(id)
                .map(LessonLogDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sổ đầu bài id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonLogDTO> findByClassId(Long classId) {
        return lessonLogRepository.findByClassId(classId)
                .stream()
                .map(LessonLogDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LessonLogDTO save(LessonLogRequest request) {
        ClassEntity classEntity = classEntityRepository.findById(request.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp id: " + request.getClassId()));

        Employee teacher = employeeRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giáo viên id: " + request.getTeacherId()));

        LessonLog lessonLog = new LessonLog();
        lessonLog.setClassEntity(classEntity);
        lessonLog.setTeacher(teacher);
        lessonLog.setTeachingDate(request.getTeachingDate());
        lessonLog.setPeriod(request.getPeriod());
        lessonLog.setContent(request.getContent());

        if (request.getSubjectId() != null) {
            Subject subject = subjectRepository.findById(request.getSubjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học id: " + request.getSubjectId()));
            lessonLog.setSubject(subject);
        }

        LessonLog saved = lessonLogRepository.save(lessonLog);
        return lessonLogRepository.findWithDetailsById(saved.getId())
                .map(LessonLogDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi khi load sổ đầu bài sau khi save"));
    }

    @Override
    @Transactional
    public LessonLogDTO updateById(Long id, LessonLogRequest request) {
        LessonLog lessonLog = lessonLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sổ đầu bài id: " + id));

        lessonLog.setTeachingDate(request.getTeachingDate());
        lessonLog.setPeriod(request.getPeriod());
        lessonLog.setContent(request.getContent());

        if (request.getClassId() != null) {
            ClassEntity classEntity = classEntityRepository.findById(request.getClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp id: " + request.getClassId()));
            lessonLog.setClassEntity(classEntity);
        }

        if (request.getTeacherId() != null) {
            Employee teacher = employeeRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giáo viên id: " + request.getTeacherId()));
            lessonLog.setTeacher(teacher);
        }

        if (request.getSubjectId() != null) {
            Subject subject = subjectRepository.findById(request.getSubjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học id: " + request.getSubjectId()));
            lessonLog.setSubject(subject);
        } else {
            lessonLog.setSubject(null);
        }

        LessonLog updated = lessonLogRepository.save(lessonLog);
        return lessonLogRepository.findWithDetailsById(updated.getId())
                .map(LessonLogDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi khi load sổ đầu bài sau khi update"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        lessonLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sổ đầu bài id: " + id));
        lessonLogRepository.deleteById(id);
    }
}
