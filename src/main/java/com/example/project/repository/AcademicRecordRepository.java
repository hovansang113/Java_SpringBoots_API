package com.example.project.repository;

import com.example.project.entity.AcademicRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface AcademicRecordRepository extends JpaRepository<AcademicRecord, Long> {

    @Query("SELECT ar FROM AcademicRecord ar " +
           "JOIN FETCH ar.student s " +
           "WHERE s.id = :studentId")
    List<AcademicRecord> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT ar FROM AcademicRecord ar " +
           "JOIN FETCH ar.student s " +
           "WHERE s.fullName LIKE %:studentName%")
    List<AcademicRecord> findByStudentName(@Param("studentName") String studentName);

    @Query("SELECT ar FROM AcademicRecord ar " +
           "JOIN FETCH ar.student " +
           "WHERE ar.schoolYear = :schoolYear AND ar.semester = :semester")
    List<AcademicRecord> findBySchoolYearAndSemester(@Param("schoolYear") String schoolYear,
                                                      @Param("semester") String semester);

    @Query("SELECT ar FROM AcademicRecord ar " +
           "JOIN FETCH ar.student " +
           "WHERE ar.id = :id")
    Optional<AcademicRecord> findWithDetailsById(@Param("id") Long id);

    @Query("SELECT ar FROM AcademicRecord ar JOIN FETCH ar.student")
    List<AcademicRecord> findAllWithDetails();
}
