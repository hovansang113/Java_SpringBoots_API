package com.example.project.repository;

import com.example.project.entity.LessonLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface LessonLogRepository extends JpaRepository<LessonLog, Long> {

    @Query("SELECT ll FROM LessonLog ll " +
           "JOIN FETCH ll.classEntity " +
           "JOIN FETCH ll.teacher " +
           "LEFT JOIN FETCH ll.subject")
    List<LessonLog> findAllWithDetails();

    @Query("SELECT ll FROM LessonLog ll " +
           "JOIN FETCH ll.classEntity " +
           "JOIN FETCH ll.teacher " +
           "LEFT JOIN FETCH ll.subject " +
           "WHERE ll.id = :id")
    Optional<LessonLog> findWithDetailsById(@Param("id") Long id);

    @Query("SELECT ll FROM LessonLog ll " +
           "JOIN FETCH ll.classEntity c " +
           "JOIN FETCH ll.teacher " +
           "LEFT JOIN FETCH ll.subject " +
           "WHERE c.id = :classId")
    List<LessonLog> findByClassId(@Param("classId") Long classId);
}
