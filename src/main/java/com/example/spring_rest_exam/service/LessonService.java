package com.example.spring_rest_exam.service;

import com.example.spring_rest_exam.dto.LessonRequest;
import com.example.spring_rest_exam.dto.response.LessonResponse;
import com.example.spring_rest_exam.dto.responseView.LessonResponseView;
import com.example.spring_rest_exam.exception.NotFoundException;
import com.example.spring_rest_exam.model.Course;
import com.example.spring_rest_exam.model.Lesson;
import com.example.spring_rest_exam.repository.CourseRepository;
import com.example.spring_rest_exam.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public LessonService(LessonRepository lessonRepository, CourseRepository courseRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
    }

    public LessonResponse addLesson(LessonRequest lesson) {
        Lesson lesson1 = mapToEntity(lesson);
        return mapToResponse(lessonRepository.save(lesson1));
    }

    public Lesson mapToEntity(LessonRequest request) {
        Lesson lesson = new Lesson();
        System.out.println(request.getCourseId());
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException(String.format("course with id - %s not found", request.getCourseId()))
        );
        lesson.setLessonName(request.getLessonName());
        lesson.setCourse(course);
        return lessonRepository.save(lesson);
    }

    public LessonResponse mapToResponse(Lesson lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .lessonName(lesson.getLessonName())
                .build();
    }

    public LessonResponse getById(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("lesson with id - %s not found", id))
        );
        return mapToResponse(lesson);
    }

 public List<LessonResponse> getAll(){
        List<LessonResponse > responses = new ArrayList<>();
     for (Lesson l:lessonRepository.findAll()) {
         responses.add(mapToResponse(l));
     }
     return responses;
 }

    public LessonResponse updateLesson(Long id, LessonRequest lesson) {
        Lesson lesson1 = lessonRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("lesson with id - %s not found", id))
        );
        Lesson lesson2 = update(lesson1, lesson);
        return mapToResponse(lesson2);
    }

    public Lesson update(Lesson lesson, LessonRequest request) {
        lesson.setLessonName(request.getLessonName());
        return lessonRepository.save(lesson);
    }

    public LessonResponse deleteLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("lesson with id - %s not found", id))
        );
        lesson.setCourse(null);
        lessonRepository.delete(lesson);
        return mapToResponse(lesson);
    }

    public List<Lesson> search(String lessonName, Pageable pageable) {
        String text = lessonName == null ? "" : lessonName;
        return lessonRepository.searchByLessonName(text.toUpperCase(), pageable);
    }

    public List<LessonResponse> getAll(List<Lesson> lessons) {
        List<LessonResponse> lessonResponses = new ArrayList<>();
        for (Lesson l : lessons) {
            lessonResponses.add(mapToResponse(l));
        }
        return lessonResponses;
    }

    public LessonResponseView pagination(String text, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        LessonResponseView lessonResponseView = new LessonResponseView();
        lessonResponseView.setResponseList(getAll(search(text, pageable)));
        return lessonResponseView;
    }
}
