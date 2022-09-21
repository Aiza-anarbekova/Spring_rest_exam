package com.example.spring_rest_exam.repository;

import com.example.spring_rest_exam.model.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    List <Student> findStudentsByCompanyId(Long id);
    List<Student> findStudentByCourseId(Long id);
    Student findStudentById(Long id);

    @Query("select s from Student s where upper(s.firstName) like concat('%',:text,'%')" +
            "or upper(s.lastName) like concat('%',:text,'%') ")
    List<Student> searchByEmail(String text, Pageable pageable);

}
