package com.example.spring_rest_exam.repository;

import com.example.spring_rest_exam.model.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {

    @Query("select s from Student s where upper(s.firstName) like concat('%',:text,'%')" +
            "or upper(s.lastName) like concat('%',:text,'%') ")
    List<Student> searchByEmail(String text, Pageable pageable);

    Student findStudentByUserEmail(String email);

    @Query("select case when count(a)>0 then true else false end" +
            " from User a where a.email =?1")
    boolean existsByUserEmail(String email);

}
