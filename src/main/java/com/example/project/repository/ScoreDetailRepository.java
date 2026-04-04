package com.example.project.repository;

import com.example.project.entity.ScoreDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ScoreDetailRepository extends JpaRepository<ScoreDetail, Long> {

    @Query("SELECT sd FROM ScoreDetail sd " +
           "JOIN FETCH sd.student " +
           "JOIN FETCH sd.subject")
    List<ScoreDetail> findAllWithDetails();

    @Query("SELECT sd FROM ScoreDetail sd " +
           "JOIN FETCH sd.student " +
           "JOIN FETCH sd.subject " +
           "WHERE sd.id = :id")
    Optional<ScoreDetail> findWithDetailsById(@Param("id") Long id);

    @Query("SELECT sd FROM ScoreDetail sd " +
           "JOIN FETCH sd.student s " +
           "JOIN FETCH sd.subject " +
           "WHERE s.id = :studentId")
    List<ScoreDetail> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT sd FROM ScoreDetail sd " +
           "JOIN FETCH sd.student s " +
           "JOIN FETCH sd.subject " +
           "WHERE s.id = :studentId " +
           "AND sd.schoolYear = :schoolYear " +
           "AND sd.semester = :semester")
    List<ScoreDetail> findByStudentIdAndSemester(@Param("studentId") Long studentId,
                                                  @Param("schoolYear") String schoolYear,
                                                  @Param("semester") String semester);
}
