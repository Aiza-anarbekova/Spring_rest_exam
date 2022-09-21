package com.example.spring_rest_exam.service;

import com.example.spring_rest_exam.dto.AssignStudentRequest;
import com.example.spring_rest_exam.dto.StudentRequest;
import com.example.spring_rest_exam.dto.response.StudentResponse;
import com.example.spring_rest_exam.dto.responseView.StudentResponseView;
import com.example.spring_rest_exam.exception.NotFoundException;
import com.example.spring_rest_exam.model.Company;
import com.example.spring_rest_exam.model.Course;
import com.example.spring_rest_exam.model.Student;
import com.example.spring_rest_exam.repository.CompanyRepository;
import com.example.spring_rest_exam.repository.CourseRepository;
import com.example.spring_rest_exam.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, CompanyRepository companyRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.courseRepository = courseRepository;
    }

    public StudentResponse addStudents(StudentRequest student) {
      Student student1 = mapToEntity(student);
      return mapToResponse(studentRepository.save(student1));
    }

    public Student mapToEntity(StudentRequest request){
        Student student = new Student();
        Company company = companyRepository.findById(request.getCompanyId()).orElseThrow(
                () -> new NotFoundException(String.format("course with id - %s not found!"
                        , request.getCompanyId())));
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setPhoneNumber(request.getPhoneNumber());
        student.setStudyFormat(request.getStudyFormat());
        student.setCompany(company);
        return studentRepository.save(student);
    }

    public StudentResponse mapToResponse(Student student){
        return StudentResponse.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .phoneNumber(student.getPhoneNumber())
                .studyFormat(student.getStudyFormat())
                .build();
    }

    public StudentResponse getById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                ()->new NotFoundException(String.format("student with id - %s not found!",id))
        );
        return mapToResponse(student);
    }

    public List<Student> getStudentByCourseId(Long id) {
        return studentRepository.findStudentByCourseId(id);
    }

    public List<Student> getStudentByCompanyId(Long id) {
        return studentRepository.findStudentsByCompanyId(id);
    }

    public Student updateStudent(Student student1,StudentRequest student) {
        student1.setFirstName(student.getFirstName());
        student1.setLastName(student.getLastName());
        student1.setPhoneNumber(student.getPhoneNumber());
        student1.setEmail(student.getEmail());
        student1.setStudyFormat(student.getStudyFormat());
        return studentRepository.save(student1);
    }

    public StudentResponse updateById(Long id,StudentRequest request){
        Student student = studentRepository.findById(id).orElseThrow(
                ()->new NotFoundException(String.format("student with id %s not found",id)));
        Student student1 = updateStudent(student,request);
        return mapToResponse(studentRepository.save(student1));
    }

    public StudentResponse deleteStudent(Long id) {
        Student student = studentRepository.findStudentById(id);
        student.setCourse(null);
        studentRepository.delete(student);
        return mapToResponse(student);
    }

    public String assignStudentToCourse(AssignStudentRequest request) {
        Student student = studentRepository.findStudentById(request.getStudentId());
        Course course = courseRepository.findCourseById(request.getCourseId());
        student.setCourse(course);
        course.addStudent(student);
        studentRepository.save(student);
        return String.format(
                "student with id %s successful assign courseId %s",
                request.getStudentId(),request.getCourseId());
    }

    public List<Student> search(String name, Pageable pageable){
        String text = name== null?"":name;
        return studentRepository.searchByEmail(text,pageable);
    }

    public List<StudentResponse> getAll(List<Student> students){
        List<StudentResponse> responses = new ArrayList<>();
        for (Student s:students) {
            responses.add(mapToResponse(s));
        }
        return responses;
    }

    public StudentResponseView pagination(String text,int size, int page){
        Pageable pageable = PageRequest.of(page-1,size);
        StudentResponseView studentResponseView = new StudentResponseView();
        studentResponseView.setResponses(getAll(search(text.toUpperCase(),pageable)));
        return studentResponseView;
    }
}