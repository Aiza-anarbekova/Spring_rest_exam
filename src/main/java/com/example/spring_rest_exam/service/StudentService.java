package com.example.spring_rest_exam.service;

import com.example.spring_rest_exam.dto.AssignStudentRequest;
import com.example.spring_rest_exam.dto.StudentRequest;
import com.example.spring_rest_exam.dto.response.StudentResponse;
import com.example.spring_rest_exam.dto.responseView.StudentResponseView;
import com.example.spring_rest_exam.exception.BadRequestException;
import com.example.spring_rest_exam.exception.NotFoundException;
import com.example.spring_rest_exam.model.Company;
import com.example.spring_rest_exam.model.Course;
import com.example.spring_rest_exam.model.Student;
import com.example.spring_rest_exam.model.User;
import com.example.spring_rest_exam.model.enums.Role;
import com.example.spring_rest_exam.repository.CompanyRepository;
import com.example.spring_rest_exam.repository.CourseRepository;
import com.example.spring_rest_exam.repository.StudentRepository;
import com.example.spring_rest_exam.security.jwt.JwtTokenUtil;
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
public class StudentService {
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final CourseRepository courseRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil util;

    @Autowired
    public StudentService(StudentRepository studentRepository, CompanyRepository companyRepository, CourseRepository courseRepository,
                          PasswordEncoder passwordEncoder, JwtTokenUtil util) {
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
        this.util = util;
    }

    public StudentResponse addStudents(StudentRequest student) {
        Student student1 = mapToEntity(student);

        return mapToResponse(studentRepository.save(student1));
    }

    public Student mapToEntity(StudentRequest request) {
        Student student = new Student();
        User user = new User();
        String email = request.getEmail();
        if (studentRepository.existsByUserEmail(email)){
            throw new BadRequestException("This email is already taken!");
        }
        Company company = companyRepository.findById(request.getCompanyId()).orElseThrow(
                () -> new NotFoundException(String.format("company with id - %s not found!"
                        , request.getCompanyId())));
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setPhoneNumber(request.getPhoneNumber());
        student.setStudyFormat(request.getStudyFormat());
        student.setCompany(company);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedDate(LocalDate.now());
        user.setRole(Role.STUDENT);
        user.setEmail(request.getEmail());
        student.setUser(user);
        return studentRepository.save(student);
    }

    public StudentResponse mapToResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setEmail(student.getUser().getEmail());
        response.setFirstName(student.getFirstName());
        response.setLastName(student.getLastName());
        response.setPhoneNumber(student.getPhoneNumber());
        response.setStudyFormat(student.getStudyFormat());
        response.setId(student.getId());
        return response;
    }

    public StudentResponse getById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("student with id - %s not found!", id))
        );
        return mapToResponse(student);
    }

    public List<StudentResponse> getAll() {
        List<StudentResponse> responses = new ArrayList<>();
        for (Student s : studentRepository.findAll()) {
            responses.add(mapToResponse(s));
        }
        return responses;
    }

    public Student updateStudent(Student student1, StudentRequest student) {
        student1.setFirstName(student.getFirstName());
        student1.setLastName(student.getLastName());
        student1.setPhoneNumber(student.getPhoneNumber());
        student1.setStudyFormat(student.getStudyFormat());
        student1.getUser().setPassword(passwordEncoder.encode(student.getPassword()));
        student1.getUser().setRole(Role.STUDENT);
        student1.getUser().setEmail(student.getEmail());
        return student1;
    }

    public StudentResponse updateById(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("student with id %s not found", id)));
        Student student1 = updateStudent(student, request);
        return mapToResponse(studentRepository.save(student1));
    }

    public StudentResponse deleteStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("student with id - %s not found!", id))
        );
        student.setCourse(null);
        studentRepository.delete(student);
        return mapToResponse(student);
    }

    public String assignStudentToCourse(AssignStudentRequest request) {
        Student student = studentRepository.findById(request.getStudentId()).orElseThrow(
                () -> new NotFoundException(String.format("student with id - %s not found!", request.getStudentId()))
        );
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException(String.format("course with id %s not found!", request.getCourseId()))
        );
        student.setCourse(course);
        course.addStudent(student);
        studentRepository.save(student);
        return String.format(
                "student with id %s successful assigned with courseId %s",
                request.getStudentId(), request.getCourseId());
    }

    public List<Student> search(String name, Pageable pageable) {
        String text = name == null ? "" : name;
        return studentRepository.searchByEmail(text, pageable);
    }

    public List<StudentResponse> getAll(List<Student> students) {
        List<StudentResponse> responses = new ArrayList<>();
        for (Student s : students) {
            responses.add(mapToResponse(s));
        }
        return responses;
    }

    public StudentResponseView pagination(String text, int size, int page) {
        Pageable pageable = PageRequest.of(page - 1, size);
        StudentResponseView studentResponseView = new StudentResponseView();
        Page<Student> students = studentRepository.findAll(pageable);
        studentResponseView.setCurrentPage(pageable.getPageNumber() + 1);
        studentResponseView.setTotalPage(students.getTotalPages());
        studentResponseView.setResponses(getAll(search(text.toUpperCase(), pageable)));
        return studentResponseView;
    }
}