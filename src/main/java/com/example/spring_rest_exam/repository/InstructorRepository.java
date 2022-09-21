package com.example.spring_rest_exam.repository;

import com.example.spring_rest_exam.model.Instructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor,Long> {
    @Query("select i from Instructor  i where i.company.id=:id")
    List<Instructor> findInstructorsByCompanyId(Long id);

//    Optional<Instructor> findInstructorsByCourseId(Long id);
    Instructor findInstructorById(Long id);

    @Query("select  i from  Instructor i where upper(i.firstName) like concat('%',:text,'%')")
    List<Instructor > searchByEmail(String text, Pageable pageable);
    }

