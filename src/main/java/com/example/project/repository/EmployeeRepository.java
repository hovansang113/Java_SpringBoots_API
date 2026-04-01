package com.example.project.repository;

import com.example.project.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT DISTINCT e FROM Employee e " +
           "LEFT JOIN FETCH e.school " +
           "LEFT JOIN FETCH e.subject " +
           "LEFT JOIN FETCH e.homeroomClasses")
    @QueryHints(@QueryHint(name = "hibernate.query.passDistinctThrough", value = "false"))
    List<Employee> findAllWithDetails();

    @Query("SELECT e FROM Employee e " +
           "LEFT JOIN FETCH e.school " +
           "LEFT JOIN FETCH e.subject " +
           "LEFT JOIN FETCH e.homeroomClasses " +
           "WHERE e.id = :id")
    Optional<Employee> findWithDetailsById(@Param("id") Long id);
}
