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
@Query("select i from Instructor i join i.courses c where i.id=:id")
    List<Instructor> findInstructorsByCoursesId(Long id);
    Instructor findInstructorById(Long id);

    @Query("select  i from  Instructor i where upper(i.firstName) like concat('%',:text,'%')")
    List<Instructor > searchByEmail(String text, Pageable pageable);

    @Query("select case when count(a)>0 then true else false end" +
            " from User a where a.email =?1")
    boolean existsByUserEmail(String email);
    }

