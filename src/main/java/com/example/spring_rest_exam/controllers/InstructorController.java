package com.example.spring_rest_exam.controllers;

import com.example.spring_rest_exam.dto.AssignInsRequest;
import com.example.spring_rest_exam.dto.InstructorRequest;
import com.example.spring_rest_exam.dto.response.InstructorResponse;
import com.example.spring_rest_exam.dto.responseView.InstructorResponseView;
import com.example.spring_rest_exam.model.Instructor;
import com.example.spring_rest_exam.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/instructor")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class InstructorController {
    private final InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorRepository) {
        this.instructorService = instructorRepository;
    }

    @PostMapping
    public InstructorResponse saveIns(@RequestBody InstructorRequest request){
        return instructorService.save(request);
    }

    @GetMapping("{id}")
    public InstructorResponse getById(@PathVariable Long id){
        return instructorService.getById(id);
    }

    @GetMapping("/all/{id}")
    public List<InstructorResponse> findInstructorByCompanyId(@PathVariable Long id){
        return instructorService.getInstructorsByCompanyId(id);
    }

    @GetMapping("/find/{id}")
    public List<InstructorResponse> findByCourseId(@PathVariable Long id){
        return instructorService.getInstructorByCourseId(id);
    }

    @PutMapping("{id}")
    public InstructorResponse updateIns(@PathVariable Long id,
                                        @RequestBody InstructorRequest request){
        return instructorService.updateById(id,request);
    }

    @DeleteMapping("/{id}")
    public InstructorResponse delete(@PathVariable("id") Long id){
        return instructorService.deleteInstructor(id);
    }

    @PostMapping("/assign")
    public String assign(@RequestBody AssignInsRequest request){
         return instructorService.assignInstructorToCourse(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','INSTRUCTOR')")
    public InstructorResponseView pagination(@RequestParam(name = "text",required = false) String text,
                                             @RequestParam int page,
                                             @RequestParam int size){
        return instructorService.pagination(text, page, size);
    }

}
