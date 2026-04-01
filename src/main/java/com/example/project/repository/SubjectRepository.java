package com.example.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import com.example.project.entity.Subject;
import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query("SELECT DISTINCT s FROM Subject s " +
           "LEFT JOIN FETCH s.school " +
           "LEFT JOIN FETCH s.teachers")
    @QueryHints(@QueryHint(name = "hibernate.query.passDistinctThrough", value = "false"))
    List<Subject> findAllWithDetails();

    @Query("SELECT s FROM Subject s " +
           "LEFT JOIN FETCH s.school " +
           "LEFT JOIN FETCH s.teachers " +
           "WHERE s.id = :id")
    Optional<Subject> findWithDetailsById(@Param("id") Long id);

    @Query("SELECT DISTINCT s FROM Subject s " +
           "LEFT JOIN FETCH s.school " +
           "LEFT JOIN FETCH s.teachers " +
           "WHERE s.name LIKE %:name%")
    @QueryHints(@QueryHint(name = "hibernate.query.passDistinctThrough", value = "false"))
    List<Subject> findAllByName(@Param("name") String name);
}
