package com.example.project.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.dto.request.ParentRequest;
import com.example.project.dto.response.ParentDTO;
import com.example.project.entity.Parent;
import com.example.project.entity.Student;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.repository.ParentRepository;
import com.example.project.repository.StudentRepository;
import com.example.project.service.ParentService;

@Service
public class ParentIplmService implements ParentService {

    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;

    public ParentIplmService(ParentRepository parentRepository, StudentRepository studentRepository) {
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParentDTO> findAll() {
        return parentRepository.findAllWithStudents()
                .stream()
                .map(ParentDTO::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ParentDTO findById(Long id) {
        Parent parent = parentRepository.findByIdWithStudents(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phụ huynh id: " + id));
        return new ParentDTO(parent);
    }

    @Override
    @Transactional
    public ParentDTO save(ParentRequest request) {
        Parent parent = new Parent();
        parent.setFullName(request.getFullName());
        parent.setPhone(request.getPhone());
        parent.setEmail(request.getEmail());
        parent.setAddress(request.getAddress());
        parent.setRelationship(request.getRelationship());

        Parent saved = parentRepository.save(parent);

        // Gán parent cho từng student trong danh sách studentIds
        if (request.getStudentIds() != null) {
            for (Long studentId : request.getStudentIds()) {
                Student student = studentRepository.findById(studentId)
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy học sinh id: " + studentId));
                student.setParent(saved);
                studentRepository.save(student);
            }
        }

        return parentRepository.findByIdWithStudents(saved.getId())
                .map(ParentDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi khi load parent sau khi save"));
    }

    @Override
    @Transactional
    public ParentDTO updateById(Long id, ParentRequest request) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phụ huynh id: " + id));

        parent.setFullName(request.getFullName());
        parent.setPhone(request.getPhone());
        parent.setEmail(request.getEmail());
        parent.setAddress(request.getAddress());
        parent.setRelationship(request.getRelationship());

        Parent updated = parentRepository.save(parent);

        // Cập nhật lại danh sách con
        if (request.getStudentIds() != null) {
            // Xóa liên kết cũ
            if (parent.getStudents() != null) {
                parent.getStudents().forEach(s -> {
                    s.setParent(null);
                    studentRepository.save(s);
                });
            }
            // Gán liên kết mới
            for (Long studentId : request.getStudentIds()) {
                Student student = studentRepository.findById(studentId)
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy học sinh id: " + studentId));
                student.setParent(updated);
                studentRepository.save(student);
            }
        }

        return parentRepository.findByIdWithStudents(updated.getId())
                .map(ParentDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi khi load parent sau khi update"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phụ huynh id: " + id));
        // Set parent_id = null cho tất cả student trước khi xóa
        if (parent.getStudents() != null) {
            parent.getStudents().forEach(s -> {
                s.setParent(null);
                studentRepository.save(s);
            });
        }
        parentRepository.deleteById(id);
    }
}
