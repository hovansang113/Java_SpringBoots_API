package com.example.project.service.impl;

import com.example.project.dto.request.ReportCardRequest;
import com.example.project.dto.response.ReportCardDTO;
import com.example.project.entity.ReportCard;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.repository.ReportCardRepository;
import com.example.project.service.ReportCardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportCardIplmService implements ReportCardService {

    private final ReportCardRepository reportCardRepository;

    public ReportCardIplmService(ReportCardRepository reportCardRepository) {
        this.reportCardRepository = reportCardRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportCardDTO> findAll() {
        return reportCardRepository.findAllWithDetails()
                .stream()
                .map(ReportCardDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReportCardDTO findById(Long id) {
        return reportCardRepository.findWithDetailsById(id)
                .map(ReportCardDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bảng điểm id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportCardDTO> findByStudentId(Long studentId) {
        return reportCardRepository.findByStudentId(studentId)
                .stream()
                .map(ReportCardDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportCardDTO> findByStudentName(String studentName) {
        return reportCardRepository.findByStudentName(studentName)
                .stream()
                .map(ReportCardDTO::new)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public ReportCardDTO updateById(Long id, ReportCardRequest request) {
        ReportCard reportCard = reportCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bảng điểm id: " + id));

        // Chỉ cập nhật điểm số
        reportCard.setScore(request.getScore());

        ReportCard updated = reportCardRepository.save(reportCard);
        return reportCardRepository.findWithDetailsById(updated.getId())
                .map(ReportCardDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi khi load bảng điểm sau khi update"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        reportCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bảng điểm id: " + id));
        reportCardRepository.deleteById(id);
    }
}
