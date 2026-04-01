package com.example.project.repository;

import com.example.project.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s " +
           "LEFT JOIN FETCH s.classEntity c " +
           "LEFT JOIN FETCH c.school " +
           "LEFT JOIN FETCH s.parent")
    List<Student> findAllWithDetails();

    @Query("SELECT s FROM Student s " +
           "LEFT JOIN FETCH s.classEntity c " +
           "LEFT JOIN FETCH c.school " +
           "LEFT JOIN FETCH s.parent " +
           "WHERE s.id = :id")
    Optional<Student> findWithDetailsById(@Param("id") Long id);
}
