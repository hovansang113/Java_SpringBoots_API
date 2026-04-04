package com.example.project.service.impl;

import com.example.project.dto.request.ScoreDetailRequest;
import com.example.project.dto.response.ScoreDetailDTO;
import com.example.project.entity.ScoreDetail;
import com.example.project.entity.Student;
import com.example.project.entity.Subject;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.repository.ReportCardRepository;
import com.example.project.repository.ScoreDetailRepository;
import com.example.project.repository.StudentRepository;
import com.example.project.repository.SubjectRepository;
import com.example.project.service.ScoreDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScoreDetailIplmService implements ScoreDetailService {

    private final ScoreDetailRepository scoreDetailRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final ReportCardRepository reportCardRepository;

    public ScoreDetailIplmService(ScoreDetailRepository scoreDetailRepository,
                                   StudentRepository studentRepository,
                                   SubjectRepository subjectRepository,
                                   ReportCardRepository reportCardRepository) {
        this.scoreDetailRepository = scoreDetailRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.reportCardRepository = reportCardRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoreDetailDTO> findAll() {
        return scoreDetailRepository.findAllWithDetails()
                .stream()
                .map(ScoreDetailDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ScoreDetailDTO findById(Long id) {
        return scoreDetailRepository.findWithDetailsById(id)
                .map(ScoreDetailDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu điểm id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoreDetailDTO> findByStudentId(Long studentId) {
        return scoreDetailRepository.findByStudentId(studentId)
                .stream()
                .map(ScoreDetailDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoreDetailDTO> findByStudentIdAndSemester(Long studentId, String schoolYear, String semester) {
        return scoreDetailRepository.findByStudentIdAndSemester(studentId, schoolYear, semester)
                .stream()
                .map(ScoreDetailDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ScoreDetailDTO save(ScoreDetailRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy học sinh id: " + request.getStudentId()));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học id: " + request.getSubjectId()));

        ScoreDetail scoreDetail = new ScoreDetail();
        scoreDetail.setStudent(student);
        scoreDetail.setSubject(subject);
        scoreDetail.setSchoolYear(request.getSchoolYear());
        scoreDetail.setSemester(request.getSemester());
        scoreDetail.setExamDate(request.getExamDate());
        scoreDetail.setExamType(request.getExamType());
        scoreDetail.setScore(request.getScore());

        ScoreDetail saved = scoreDetailRepository.save(scoreDetail);

        // Tự động cập nhật điểm TB vào ReportCard
        updateReportCardScore(student, subject, request.getSchoolYear(), request.getSemester());

        return scoreDetailRepository.findWithDetailsById(saved.getId())
                .map(ScoreDetailDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi khi load phiếu điểm sau khi save"));
    }

    @Override
    @Transactional
    public ScoreDetailDTO updateById(Long id, ScoreDetailRequest request) {
        ScoreDetail scoreDetail = scoreDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu điểm id: " + id));

        scoreDetail.setExamDate(request.getExamDate());
        scoreDetail.setExamType(request.getExamType());
        scoreDetail.setScore(request.getScore());
        scoreDetail.setSchoolYear(request.getSchoolYear());
        scoreDetail.setSemester(request.getSemester());

        ScoreDetail updated = scoreDetailRepository.save(scoreDetail);

        // Tự động cập nhật điểm TB vào ReportCard
        updateReportCardScore(scoreDetail.getStudent(), scoreDetail.getSubject(),
                request.getSchoolYear(), request.getSemester());

        return scoreDetailRepository.findWithDetailsById(updated.getId())
                .map(ScoreDetailDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi khi load phiếu điểm sau khi update"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ScoreDetail scoreDetail = scoreDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu điểm id: " + id));

        Student student = scoreDetail.getStudent();
        Subject subject = scoreDetail.getSubject();
        String schoolYear = scoreDetail.getSchoolYear();
        String semester = scoreDetail.getSemester();

        scoreDetailRepository.deleteById(id);

        // Cập nhật lại điểm TB sau khi xóa
        updateReportCardScore(student, subject, schoolYear, semester);
    }

    // Tính điểm TB từ ScoreDetail và cập nhật vào ReportCard
    private void updateReportCardScore(Student student, Subject subject, String schoolYear, String semester) {
        List<ScoreDetail> scores = scoreDetailRepository
                .findByStudentIdAndSemester(student.getId(), schoolYear, semester)
                .stream()
                .filter(sd -> sd.getSubject().getId().equals(subject.getId()))
                .collect(Collectors.toList());

        float avg = scores.isEmpty() ? 0f : (float) scores.stream()
                .mapToDouble(ScoreDetail::getScore)
                .average()
                .orElse(0.0);

        reportCardRepository.findByStudentIdAndSubjectIdAndSemester(
                student.getId(), subject.getId(), schoolYear, semester)
                .ifPresent(rc -> {
                    rc.setScore(avg);
                    reportCardRepository.save(rc);
                });
    }
}
