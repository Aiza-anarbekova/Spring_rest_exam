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

import javax.annotation.security.PermitAll;
import java.util.List;


@RestController
@RequestMapping("api/student")
@PreAuthorize("hasAuthority('ADMIN')")
public class StudentController {

    private final StudentService studentRepository;

    @Autowired
    public StudentController(StudentService studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostMapping
    public StudentResponse  saveStud(@RequestBody StudentRequest request){
        return studentRepository.addStudents(request);
    }

    @GetMapping("{id}")
    public StudentResponse getById(@PathVariable Long id){
        return studentRepository.getById(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','INSTRUCTOR')")
    public List<StudentResponse> getAll(){
        return studentRepository.getAll();
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','INSTRUCTOR')")
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
    @PreAuthorize("hasAnyAuthority('ADMIN','INSTRUCTOR')")
    public StudentResponseView pagination(@RequestParam(name = "text",required = false) String text,
                                          @RequestParam int page,
                                          @RequestParam int size){
        return studentRepository.pagination(text, page, size);
    }
}
