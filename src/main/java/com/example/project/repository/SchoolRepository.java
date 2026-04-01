package com.example.project.repository;
import com.example.project.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SchoolRepository extends JpaRepository<School, Long> {
    
}
