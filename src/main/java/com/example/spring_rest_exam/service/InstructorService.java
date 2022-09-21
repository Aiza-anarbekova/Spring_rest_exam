package com.example.spring_rest_exam.service;

import com.example.spring_rest_exam.dto.AssignInsRequest;
import com.example.spring_rest_exam.dto.response.InstAssignResponse;
import com.example.spring_rest_exam.dto.InstructorRequest;
import com.example.spring_rest_exam.dto.response.InstructorResponse;
import com.example.spring_rest_exam.dto.responseView.InstructorResponseView;
import com.example.spring_rest_exam.exception.NotFoundException;
import com.example.spring_rest_exam.model.Company;
import com.example.spring_rest_exam.model.Course;
import com.example.spring_rest_exam.model.Instructor;
import com.example.spring_rest_exam.repository.CompanyRepository;
import com.example.spring_rest_exam.repository.CourseRepository;
import com.example.spring_rest_exam.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public InstructorService(InstructorRepository instructorRepository, CourseRepository courseRepository, CompanyRepository companyRepository) {
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.companyRepository = companyRepository;
    }

    public Instructor mapToEntity(InstructorRequest instructor) {
        Instructor instructor1 = new Instructor();
        Company company = companyRepository.findById(instructor.getCompanyId()).orElseThrow(
                () -> new RuntimeException("not found!"));
        instructor1.setFirstName(instructor.getFirstName());
        instructor1.setLastName(instructor.getLastName());
        instructor1.setEmail(instructor.getEmail());
        instructor1.setPhoneNumber(instructor.getPhoneNumber());
        instructor1.setSpecialization(instructor.getSpecialization());
        instructor1.setCompany(company);
        return instructorRepository.save(instructor1);
    }

    public InstructorResponse save(InstructorRequest request) {
        Instructor instructor = mapToEntity(request);
        return mapToResponse(instructorRepository.save(instructor));
    }


    public InstructorResponse mapToResponse(Instructor instructor) {
        return InstructorResponse.builder()
                .id(instructor.getId())
                .firstName(instructor.getFirstName())
                .lastName(instructor.getLastName())
                .email(instructor.getEmail())
                .specialization(instructor.getSpecialization())
                .phoneNumber(instructor.getPhoneNumber())
                .build();
    }

    public InstructorResponse getById(Long id) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("instructor not found %s", id))
        );

        return mapToResponse(instructor);
    }

    public List<Instructor> getInstructorByCourseId(Long id) {
        return courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("course with id - %s not found ",id))).getInstructors();
    }

    public List<Instructor> getInstructorsByCompanyId(Long id){
        return companyRepository.findById(id).orElseThrow(
                ()->new NotFoundException(String.format("company with id %s not found",id)
                )).getInstructors();
    }

    public Instructor update(Instructor instructor1, InstructorRequest instructor) {
        instructor1.setFirstName(instructor.getFirstName());
        instructor1.setLastName(instructor.getLastName());
        instructor1.setEmail(instructor.getEmail());
        instructor1.setPhoneNumber(instructor.getPhoneNumber());
        instructor1.setSpecialization(instructor.getSpecialization());
        return instructorRepository.save(instructor1);
    }

    public InstructorResponse updateById(Long id, InstructorRequest request) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("not found = %s", id)));
        Instructor instructor1 = update(instructor, request);
        return mapToResponse(instructorRepository.save(instructor1));
    }

    public InstructorResponse deleteInstructor(Long id) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("not found = %s", id))
        );
        for (Course c : instructor.getCourses()) {
            c.setInstructors(null);
        }
        instructorRepository.delete(instructor);
        return mapToResponse(instructor);
    }

    public String assignInstructorToCourse(AssignInsRequest request) {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                ()->new NotFoundException(String.format("course with id - %s not found!",request.getCourseId()))
        );
        Instructor instructor = instructorRepository.findById(request.getInsId()).orElseThrow(
                ()->new NotFoundException(String.format("instructor with id - %s not found",request.getInsId()))
        );
        instructor.addCourse(course);
        course.addInstructor(instructor);
        instructorRepository.save(instructor);
        return String.format("instructor with id - %s successful assign  course with - %s",request.getInsId(),request.getCourseId());
    }

    public List<Instructor> search(String name, Pageable pageable){
        String text = name==null?"":name;
        return instructorRepository.searchByEmail(text.toUpperCase(),pageable);
    }

    public List<InstructorResponse> getAll(List<Instructor> instructors){
        List<InstructorResponse> responses = new ArrayList<>();
        for (Instructor i:instructors) {
            responses.add(mapToResponse(i));
        }
        return responses;
    }

    public InstructorResponseView pagination(String text, int page,int size){
        Pageable pageable = PageRequest.of(page-1, size);
        InstructorResponseView instructorResponseView = new InstructorResponseView();
        instructorResponseView.setResponses(getAll(search(text,pageable)));
        return instructorResponseView;
    }
}
