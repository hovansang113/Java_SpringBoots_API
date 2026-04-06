package com.example.project.service.impl;

import com.example.project.dto.request.StudentCardRequest;
import com.example.project.dto.response.StudentCardDTO;
import com.example.project.entity.Student;
import com.example.project.entity.StudentCard;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.repository.StudentCardRepository;
import com.example.project.repository.StudentRepository;
import com.example.project.service.StudentCardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentCardIplmService implements StudentCardService {

    private final StudentCardRepository studentCardRepository;
    private final StudentRepository studentRepository;

    public StudentCardIplmService(StudentCardRepository studentCardRepository,
                                   StudentRepository studentRepository) {
        this.studentCardRepository = studentCardRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentCardDTO> findAll() {
        return studentCardRepository.findAllWithDetails()
                .stream()
                .map(StudentCardDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudentCardDTO findById(Long id) {
        return studentCardRepository.findWithDetailsById(id)
                .map(StudentCardDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thẻ học sinh id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public StudentCardDTO findByStudentId(Long studentId) {
        return studentCardRepository.findByStudentId(studentId)
                .map(StudentCardDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thẻ học sinh của student id: " + studentId));
    }

    @Override
    @Transactional
    public StudentCardDTO save(StudentCardRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy học sinh id: " + request.getStudentId()));

        // Kiểm tra học sinh đã có thẻ chưa
        studentCardRepository.findByStudentId(request.getStudentId()).ifPresent(sc -> {
            throw new RuntimeException("Học sinh id: " + request.getStudentId() + " đã có thẻ học sinh");
        });

        StudentCard studentCard = new StudentCard();
        studentCard.setStudent(student);
        studentCard.setCardCode(generateCardCode());
        studentCard.setAvatar(request.getAvatar());
        studentCard.setIssueDate(request.getIssueDate());

        StudentCard saved = studentCardRepository.save(studentCard);
        return studentCardRepository.findWithDetailsById(saved.getId())
                .map(StudentCardDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi khi load thẻ học sinh sau khi save"));
    }

    private String generateCardCode() {
        String cardCode;
        do {
            cardCode = String.format("%08d", (int)(Math.random() * 100000000));
        } while (studentCardRepository.findByCardCode(cardCode).isPresent());
        return cardCode;
    }

    @Override
    @Transactional
    public StudentCardDTO updateById(Long id, StudentCardRequest request) {
        StudentCard studentCard = studentCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thẻ học sinh id: " + id));

        studentCard.setAvatar(request.getAvatar());
        studentCard.setIssueDate(request.getIssueDate());

        StudentCard updated = studentCardRepository.save(studentCard);
        return studentCardRepository.findWithDetailsById(updated.getId())
                .map(StudentCardDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi khi load thẻ học sinh sau khi update"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        studentCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thẻ học sinh id: " + id));
        studentCardRepository.deleteById(id);
    }
}
