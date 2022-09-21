package com.example.spring_rest_exam.repository;

import com.example.spring_rest_exam.model.Lesson;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface LessonRepository extends JpaRepository<Lesson,Long> {
    List<Lesson> findLessonByCourseId(Long id);
    Lesson findLessonById(Long id);

    @Query("select l from Lesson l where upper(l.lessonName) like concat('%',:text,'%') ")
    List<Lesson> searchByLessonName(String text, Pageable pageable);
}
