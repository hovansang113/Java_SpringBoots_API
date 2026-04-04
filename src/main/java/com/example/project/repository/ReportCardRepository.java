package com.example.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.project.entity.ReportCard;
import java.util.List;
import java.util.Optional;

public interface ReportCardRepository extends JpaRepository<ReportCard, Long> {

    @Query("SELECT r FROM ReportCard r " +
           "JOIN FETCH r.student s " +
           "JOIN FETCH r.subject " +
           "WHERE s.id = :studentId")
    List<ReportCard> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT r FROM ReportCard r " +
           "JOIN FETCH r.student s " +
           "JOIN FETCH r.subject " +
           "WHERE s.fullName LIKE %:studentName%")
    List<ReportCard> findByStudentName(@Param("studentName") String studentName);

    @Query("SELECT r FROM ReportCard r " +
           "JOIN FETCH r.student " +
           "JOIN FETCH r.subject " +
           "WHERE r.id = :id")
    Optional<ReportCard> findWithDetailsById(@Param("id") Long id);

    @Query("SELECT r FROM ReportCard r " +
           "JOIN FETCH r.student " +
           "JOIN FETCH r.subject")
    List<ReportCard> findAllWithDetails();

    @Query("SELECT r FROM ReportCard r " +
           "WHERE r.student.id = :studentId " +
           "AND r.subject.id = :subjectId " +
           "AND r.schoolYear = :schoolYear " +
           "AND r.semester = :semester")
    Optional<ReportCard> findByStudentIdAndSubjectIdAndSemester(
            @Param("studentId") Long studentId,
            @Param("subjectId") Long subjectId,
            @Param("schoolYear") String schoolYear,
            @Param("semester") String semester);

    @Query("SELECT r FROM ReportCard r " +
           "WHERE r.student.id = :studentId " +
           "AND r.schoolYear = :schoolYear " +
           "AND r.semester = :semester")
    List<ReportCard> findByStudentIdAndSemester(
            @Param("studentId") Long studentId,
            @Param("schoolYear") String schoolYear,
            @Param("semester") String semester);
}
