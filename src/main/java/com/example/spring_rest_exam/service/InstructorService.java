package com.example.spring_rest_exam.service;

import com.example.spring_rest_exam.dto.AssignInsRequest;
import com.example.spring_rest_exam.dto.InstructorRequest;
import com.example.spring_rest_exam.dto.response.InstructorResponse;
import com.example.spring_rest_exam.dto.responseView.InstructorResponseView;
import com.example.spring_rest_exam.exception.BadRequestException;
import com.example.spring_rest_exam.exception.NotFoundException;
import com.example.spring_rest_exam.model.Company;
import com.example.spring_rest_exam.model.Course;
import com.example.spring_rest_exam.model.Instructor;
import com.example.spring_rest_exam.model.User;
import com.example.spring_rest_exam.model.enums.Role;
import com.example.spring_rest_exam.repository.CompanyRepository;
import com.example.spring_rest_exam.repository.CourseRepository;
import com.example.spring_rest_exam.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public InstructorService(InstructorRepository instructorRepository, CourseRepository courseRepository,
                             CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public Instructor mapToEntity(InstructorRequest instructor) {
        Instructor instructor1 = new Instructor();
        User user = new User();
        String email = instructor.getEmail();
        if (instructorRepository.existsByUserEmail(email)){
            throw new BadRequestException(("this email  is already taken!"));
        }
        Company company = companyRepository.findById(instructor.getCompanyId()).orElseThrow(
                () -> new RuntimeException("not found!"));
        instructor1.setFirstName(instructor.getFirstName());
        instructor1.setLastName(instructor.getLastName());
        instructor1.setPhoneNumber(instructor.getPhoneNumber());
        instructor1.setSpecialization(instructor.getSpecialization());
        user.setCreatedDate(LocalDate.now());
        user.setEmail(instructor.getEmail());
        user.setPassword(passwordEncoder.encode(instructor.getPassword()));
        user.setRole(Role.INSTRUCTOR);
        instructor1.setCompany(company);
        instructor1.setUser(user);
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
                .email(instructor.getUser().getEmail())
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

    public List<InstructorResponse> getInstructorByCourseId(Long id) {
      List<InstructorResponse> responses = new ArrayList<>();
        for (Instructor instructor : instructorRepository.findInstructorsByCoursesId(id)) {
            responses.add(mapToResponse(instructor));
        }
        return responses;
    }

    public List<InstructorResponse> getInstructorsByCompanyId(Long id){
        List<InstructorResponse> responses = new ArrayList<>();
        for (Instructor instructor : instructorRepository.findInstructorsByCompanyId(id)) {
            responses.add(mapToResponse(instructor));
        }
        return responses;
    }

    public Instructor update(Instructor instructor1, InstructorRequest instructor) {
        instructor1.setFirstName(instructor.getFirstName());
        instructor1.setLastName(instructor.getLastName());
        instructor1.setPhoneNumber(instructor.getPhoneNumber());
        instructor1.setSpecialization(instructor.getSpecialization());
        instructor1.getUser().setEmail(instructor.getEmail());
        instructor1.getUser().setPassword(passwordEncoder.encode(instructor.getPassword()));
        instructor1.getUser().setRole(Role.INSTRUCTOR);
        return instructor1;
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
        Page<Instructor> responseViews = instructorRepository.findAll(pageable);
        instructorResponseView.setCurrentPage(pageable.getPageNumber()+1);
        instructorResponseView.setTotalPage(responseViews.getTotalPages());
        instructorResponseView.setResponses(getAll(search(text,pageable)));
        return instructorResponseView;
    }

}
