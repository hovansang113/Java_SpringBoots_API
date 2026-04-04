package com.example.project.service.impl;

import com.example.project.dto.request.AcademicRecordRequest;
import com.example.project.dto.response.AcademicRecordDTO;
import com.example.project.entity.AcademicRecord;
import com.example.project.entity.Student;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.repository.AcademicRecordRepository;
import com.example.project.repository.ReportCardRepository;
import com.example.project.repository.StudentRepository;
import com.example.project.service.AcademicRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AcademicReportIplmService implements AcademicRecordService {

    private final AcademicRecordRepository academicRecordRepository;
    private final StudentRepository studentRepository;
    private final ReportCardRepository reportCardRepository;

    public AcademicReportIplmService(AcademicRecordRepository academicRecordRepository,
                                      StudentRepository studentRepository,
                                      ReportCardRepository reportCardRepository) {
        this.academicRecordRepository = academicRecordRepository;
        this.studentRepository = studentRepository;
        this.reportCardRepository = reportCardRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicRecordDTO> findAll() {
        return academicRecordRepository.findAllWithDetails()
                .stream()
                .map(AcademicRecordDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AcademicRecordDTO findById(Long id) {
        return academicRecordRepository.findWithDetailsById(id)
                .map(AcademicRecordDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sổ học bạ id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicRecordDTO> findByStudentId(Long studentId) {
        return academicRecordRepository.findByStudentId(studentId)
                .stream()
                .map(AcademicRecordDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicRecordDTO> findByStudentName(String studentName) {
        return academicRecordRepository.findByStudentName(studentName)
                .stream()
                .map(AcademicRecordDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicRecordDTO> findBySchoolYearAndSemester(String schoolYear, String semester) {
        return academicRecordRepository.findBySchoolYearAndSemester(schoolYear, semester)
                .stream()
                .map(AcademicRecordDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AcademicRecordDTO save(AcademicRecordRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy học sinh id: " + request.getStudentId()));

        // Tự động tính điểm TB từ ReportCard của học sinh trong kỳ
        float averageScore = calculateAverageScore(request.getStudentId(),
                request.getSchoolYear(), request.getSemester());

        AcademicRecord academicRecord = new AcademicRecord();
        academicRecord.setStudent(student);
        academicRecord.setSchoolYear(request.getSchoolYear());
        academicRecord.setSemester(request.getSemester());
        academicRecord.setAverageScore(averageScore);
        academicRecord.setConduct(request.getConduct());
        academicRecord.setTeacherComment(request.getTeacherComment());
        academicRecord.setParentComment(request.getParentComment());

        AcademicRecord saved = academicRecordRepository.save(academicRecord);
        return academicRecordRepository.findWithDetailsById(saved.getId())
                .map(AcademicRecordDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi khi load sổ học bạ sau khi save"));
    }

    @Override
    @Transactional
    public AcademicRecordDTO updateById(Long id, AcademicRecordRequest request) {
        AcademicRecord academicRecord = academicRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sổ học bạ id: " + id));

        // Tự động tính lại điểm TB
        float averageScore = calculateAverageScore(academicRecord.getStudent().getId(),
                request.getSchoolYear(), request.getSemester());

        academicRecord.setSchoolYear(request.getSchoolYear());
        academicRecord.setSemester(request.getSemester());
        academicRecord.setAverageScore(averageScore);
        academicRecord.setConduct(request.getConduct());
        academicRecord.setTeacherComment(request.getTeacherComment());
        academicRecord.setParentComment(request.getParentComment());

        AcademicRecord updated = academicRecordRepository.save(academicRecord);
        return academicRecordRepository.findWithDetailsById(updated.getId())
                .map(AcademicRecordDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi khi load sổ học bạ sau khi update"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        academicRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sổ học bạ id: " + id));
        academicRecordRepository.deleteById(id);
    }

    // Tính điểm TB từ tất cả ReportCard của học sinh trong kỳ
    private float calculateAverageScore(Long studentId, String schoolYear, String semester) {
        return (float) reportCardRepository
                .findByStudentIdAndSemester(studentId, schoolYear, semester)
                .stream()
                .filter(rc -> rc.getScore() != null)
                .mapToDouble(rc -> rc.getScore())
                .average()
                .orElse(0.0);
    }
}
