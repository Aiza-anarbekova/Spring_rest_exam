package com.example.spring_rest_exam.repository;

import com.example.spring_rest_exam.dto.response.CourseResponse;
import com.example.spring_rest_exam.model.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByCompanyId(Long id);
@Query("select c from Course c where c.company.id=:id")
List<Course> findCourseByCompanyId(Long id);

    Course findCourseById(Long id);

    @Query("select c from  Course c where upper(c.name) like concat('%',:text,'%')")
    List<Course > searchCourseByName(String text , Pageable pageable);
}
