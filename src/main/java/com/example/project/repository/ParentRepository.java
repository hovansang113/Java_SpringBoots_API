package com.example.project.repository;

import com.example.project.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;
public interface ParentRepository extends JpaRepository<Parent, Long> {
    @Query("SELECT DISTINCT p FROM Parent p " +
       "LEFT JOIN FETCH p.students s " +
       "LEFT JOIN FETCH s.classEntity c " +
       "LEFT JOIN FETCH c.school")
    List<Parent> findAllWithStudents();

    @Query(
        "SELECT p FROM Parent p " +
        "LEFT JOIN FETCH p.students s " +
        "LEFT JOIN FETCH s.classEntity c " +
        "LEFT JOIN FETCH c.school " +
        "WHERE p.id = :id"
    )
    Optional<Parent> findByIdWithStudents(@Param("id") Long id);


}
