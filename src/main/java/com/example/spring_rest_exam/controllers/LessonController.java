package com.example.spring_rest_exam.controllers;

import com.example.spring_rest_exam.dto.LessonRequest;
import com.example.spring_rest_exam.dto.response.LessonResponse;
import com.example.spring_rest_exam.dto.responseView.LessonResponseView;
import com.example.spring_rest_exam.model.Lesson;
import com.example.spring_rest_exam.service.LessonService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/lesson")
@PreAuthorize("hasAuthority('INSTRUCTOR')")
public class LessonController {
    private final LessonService lessonService;

    public LessonController(LessonService lessonRepository) {
        this.lessonService = lessonRepository;
    }


    @PostMapping
    public LessonResponse saveLesson(@RequestBody LessonRequest request){
        return lessonService.addLesson(request);
    }

    @GetMapping("{id}")
    public LessonResponse getById(@PathVariable Long id){
        return lessonService.getById(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('STUDENT','INSTRUCTOR')")
    public List<LessonResponse> getAll(){
        return lessonService.getAll();
    }


    @PutMapping("/{id}")
    public LessonResponse updateLesson(@PathVariable Long id , @RequestBody LessonRequest request){
        return lessonService.updateLesson(id, request);
    }

    @DeleteMapping("/{id}")
    public LessonResponse delete(@PathVariable("id") Long id){
        return lessonService.deleteLesson(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    public LessonResponseView pagination(@RequestParam (name = "text") String text,
                                         @RequestParam int page,
                                         @RequestParam int size){
        return lessonService.pagination(text, page, size);
    }
}
