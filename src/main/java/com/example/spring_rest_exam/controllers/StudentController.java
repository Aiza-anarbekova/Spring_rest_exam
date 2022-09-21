package com.example.spring_rest_exam.controllers;

import com.example.spring_rest_exam.dto.AssignStudentRequest;
import com.example.spring_rest_exam.dto.StudentRequest;
import com.example.spring_rest_exam.dto.response.StudentResponse;
import com.example.spring_rest_exam.dto.responseView.StudentResponseView;
import com.example.spring_rest_exam.model.Course;
import com.example.spring_rest_exam.model.Student;
import com.example.spring_rest_exam.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/student")
@PreAuthorize("hasAuthority('ADMIN')")
public class StudentController {
    private final InstructorService instructorRepository;
    private final StudentService studentRepository;
    private final LessonService lessonRepository;
    private final CourseService courseRepository;
    private final TaskService taskRepositoryImpl;

    @Autowired
    public StudentController(InstructorService instructorRepository, StudentService studentRepository, LessonService lessonRepository, CourseService courseRepository, TaskService taskRepositoryImpl) {
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.taskRepositoryImpl = taskRepositoryImpl;
    }


    @PostMapping
    public StudentResponse  saveStud(@RequestBody StudentRequest request){
        return studentRepository.addStudents(request);
    }

    @GetMapping("{id}")
    public StudentResponse getById(@PathVariable Long id){
        return studentRepository.getById(id);
    }


    @PutMapping("/{id}")
    public StudentResponse updateStudent(@PathVariable Long id , @RequestBody StudentRequest request){
        return studentRepository.updateById(id,request);
    }

    @DeleteMapping("/{id}")
    public StudentResponse delete(@PathVariable("id") Long id){
        return studentRepository.deleteStudent(id);
    }

    @PostMapping("/assign")
    public String assign(@RequestBody AssignStudentRequest request){
        return studentRepository.assignStudentToCourse(request);
    }

    @GetMapping
    public StudentResponseView pagination(@RequestParam(name = "text",required = false) String text,
                                          @RequestParam int page,
                                          @RequestParam int size){
        return studentRepository.pagination(text, page, size);
    }
}
