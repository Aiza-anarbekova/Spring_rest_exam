package com.example.spring_rest_exam.service;

import com.example.spring_rest_exam.dto.TaskRequest;
import com.example.spring_rest_exam.dto.response.StudentResponse;
import com.example.spring_rest_exam.dto.response.TaskResponse;
import com.example.spring_rest_exam.dto.responseView.StudentResponseView;
import com.example.spring_rest_exam.dto.responseView.TaskResponseView;
import com.example.spring_rest_exam.exception.NotFoundException;
import com.example.spring_rest_exam.model.Lesson;
import com.example.spring_rest_exam.model.Student;
import com.example.spring_rest_exam.model.Task;
import com.example.spring_rest_exam.repository.LessonRepository;
import com.example.spring_rest_exam.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final LessonRepository lessonRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, LessonRepository lessonRepository) {
        this.taskRepository = taskRepository;
        this.lessonRepository = lessonRepository;
    }

    public TaskResponse addTask(TaskRequest request) {
        Task task = mapToEntity(request);
        return mapToResponse(taskRepository.save(task));
    }

    public Task mapToEntity(TaskRequest request) {
        Task task = new Task();
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(
                () -> new NotFoundException(String.format("lesson with id %s not found!", request.getLessonId()))
        );
        task.setName(request.getName());
        task.setDeadline(request.getDeadline());
        task.setTask(request.getTask());
        task.setLesson(lesson);
        return taskRepository.save(task);
    }

    public TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .deadline(task.getDeadline())
                .task(task.getTask())
                .build();
    }

    public TaskResponse getById(Long id) {
        Task task = taskRepository.findTaskById(id);
        return mapToResponse(task);

    }


    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("task with id - %s not found", id))
        );
        Task task1 = update(task, request);
        return mapToResponse(taskRepository.save(task1));
    }

    public Task update(Task task, TaskRequest request) {
        task.setName(request.getName());
        task.setDeadline(request.getDeadline());
        task.setTask(request.getTask());
        return taskRepository.save(task);
    }

    public TaskResponse deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                ()->new NotFoundException(String.format("task with id - %s not found",id))
        );
        task.setLesson(null);
        taskRepository.delete(task);
        return mapToResponse(task);
    }
    public List<Task> search(String name, Pageable pageable){
        String text = name== null?"":name;
        return taskRepository.searchByName(text,pageable);
    }

    public List<TaskResponse> getAll(List<Task> tasks){
        List<TaskResponse> responses = new ArrayList<>();
        for (Task t:tasks) {
            responses.add(mapToResponse(t));
        }
        return responses;
    }

    public TaskResponseView pagination(String text, int size, int page){
        Pageable pageable = PageRequest.of(page-1,size);
        TaskResponseView taskResponseView = new TaskResponseView();
        taskResponseView.setResponses(getAll(search(text.toUpperCase(),pageable)));
        return taskResponseView;
    }
}
