package com.example.project.repository;

import com.example.project.entity.StudentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface StudentCardRepository extends JpaRepository<StudentCard, Long> {

    @Query("SELECT sc FROM StudentCard sc JOIN FETCH sc.student")
    List<StudentCard> findAllWithDetails();

    @Query("SELECT sc FROM StudentCard sc JOIN FETCH sc.student WHERE sc.id = :id")
    Optional<StudentCard> findWithDetailsById(@Param("id") Long id);

    @Query("SELECT sc FROM StudentCard sc JOIN FETCH sc.student WHERE sc.student.id = :studentId")
    Optional<StudentCard> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT sc FROM StudentCard sc WHERE sc.cardCode = :cardCode")
    Optional<StudentCard> findByCardCode(@Param("cardCode") String cardCode);
}
