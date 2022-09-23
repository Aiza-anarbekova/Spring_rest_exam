package com.example.spring_rest_exam.controllers;

import com.example.spring_rest_exam.dto.TaskRequest;
import com.example.spring_rest_exam.dto.response.TaskResponse;
import com.example.spring_rest_exam.dto.responseView.StudentResponseView;
import com.example.spring_rest_exam.dto.responseView.TaskResponseView;
import com.example.spring_rest_exam.dto.responseView.VideoResponseView;
import com.example.spring_rest_exam.model.Lesson;
import com.example.spring_rest_exam.model.Task;
import com.example.spring_rest_exam.service.TaskService;
import com.example.spring_rest_exam.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/task")
@PreAuthorize("hasAuthority('INSTUCTOR')")
public class TaskController {
    private final TaskService taskService;
    @Autowired
    public TaskController(TaskService taskRepository) {
        this.taskService = taskRepository;

    }

    @PostMapping
    public TaskResponse save(@RequestBody TaskRequest request){
        return taskService.addTask(request);
    }

    @GetMapping("{id}")
    public TaskResponse getById(@PathVariable Long id){
        return taskService.getById(id);
    }


    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id ,
                                   @RequestBody TaskRequest request){
        return taskService.updateTask(id,request);
    }


    @DeleteMapping("/{id}")
    public TaskResponse delete(@PathVariable("id") Long id){
        return taskService.deleteTask(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('STUDENT','INSTRUCTOR')")
    public TaskResponseView pagination(@RequestParam(name = "text",required = false) String text,
                                       @RequestParam int page,
                                       @RequestParam int size){
        return taskService.pagination(text, page, size);
    }
}

