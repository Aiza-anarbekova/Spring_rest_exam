package com.example.spring_rest_exam.repository;

import com.example.spring_rest_exam.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findTasksById(Long id);
    List<Task> findTaskByLessonId(Long id);
    Task findTaskById(Long id);

    @Query("select t from Task t where upper(t.name) like concat('%',:text,'%') ")
    List<Task> searchByName(String text, Pageable pageable);

}
