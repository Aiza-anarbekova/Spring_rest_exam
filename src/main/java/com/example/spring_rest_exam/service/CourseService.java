package com.example.spring_rest_exam.service;

import com.example.spring_rest_exam.dto.CompanyRequest;
import com.example.spring_rest_exam.dto.CourseRequest;
import com.example.spring_rest_exam.dto.response.CompanyResponse;
import com.example.spring_rest_exam.dto.response.CourseResponse;
import com.example.spring_rest_exam.dto.responseView.CourseResponseView;
import com.example.spring_rest_exam.exception.NotFoundException;
import com.example.spring_rest_exam.model.Company;
import com.example.spring_rest_exam.model.Course;
import com.example.spring_rest_exam.model.Instructor;
import com.example.spring_rest_exam.repository.CompanyRepository;
import com.example.spring_rest_exam.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, CompanyRepository companyRepository) {
        this.courseRepository = courseRepository;
        this.companyRepository = companyRepository;
    }

    public CourseResponse addCourse(CourseRequest request) {
        Course course = mapToEntity(request);
        return mapToResponse(courseRepository.save(course));
    }

    public Course mapToEntity(CourseRequest request) {
        Course course = new Course();
        Company company = companyRepository.findById(request.getCompanyId()).orElseThrow(
                () -> new NotFoundException(String.format("company with id - %s not found!"
                        , request.getCompanyId())));
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setDuration(request.getDuration());
        course.setDateOfStart(request.getDateOfStart());
        course.setCompany(company);
        return courseRepository.save(course);
    }

    public CourseResponse mapToResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .dateOfStart(course.getDateOfStart())
                .duration(course.getDuration())
                .build();
    }

    public List<CourseResponse> getCourseByCompanyId(Long id) {
       List<CourseResponse > responses = new ArrayList<>();
        for (Course course:courseRepository.findCourseByCompanyId(id)) {
            responses.add(mapToResponse(course));
        }
        return responses;

    }

    public CourseResponse getById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("course with id - %s not found!", id)));
        return mapToResponse(course);
    }

    public CourseResponse updateByCourseId(Long id, CourseRequest course) {
        Course course1 = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("course with id - %s not found!", id)));
        Course course2 = update(course1, course);
        return mapToResponse(courseRepository.save(course2));
    }

    public Course update(Course course, CourseRequest request) {
        String courseName = course.getName();
        String newCourseName = request.getName();
        if (newCourseName != null && !newCourseName.equals(courseName)) {
            course.setName(newCourseName);
        }
        String description = course.getDescription();
        String newDescription = request.getDescription();
        if (newDescription != null && !newDescription.equals(description)) {
            course.setDescription(newDescription);
        }
        String duration = course.getDuration();
        String newDuration = request.getDuration();
        if (newDuration != null && !newDuration.equals(duration)) {
            course.setDuration(newDuration);
        }
        LocalDate dateOfStart = course.getDateOfStart();
        LocalDate newDateOfStart = request.getDateOfStart();
        if (newDateOfStart != null && !newDateOfStart.equals(dateOfStart)) {
            course.setDateOfStart(request.getDateOfStart());
        }
        return courseRepository.save(course);
    }

    public CourseResponse deleteCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("course with id -%s not found", id))
        );
        for (Instructor instructor : course.getInstructors()) {
            instructor.setCourses(null);
        }
        course.setCompany(null);
        courseRepository.delete(course);
        return mapToResponse(course);
    }

    public List<Course> search(String name, Pageable pageable) {
        String text = name == null ? "" : name;
        return  courseRepository.searchCourseByName(text.toUpperCase(), pageable);
    }

    public List<CourseResponse> getAll(List<Course> courses){
        List<CourseResponse > responses = new ArrayList<>();
        for (Course c:courses) {
            responses.add(mapToResponse(c));
        }
        return responses;
    }

    public CourseResponseView pagination(String text, int page, int size){
        Pageable pageable = PageRequest.of(page-1, size, Sort.by("name"));
        CourseResponseView courseResponseView = new CourseResponseView();
        Page<Course> coursePage=courseRepository.findAll(pageable);
        courseResponseView.setCurrentPage(pageable.getPageNumber()+1);
        courseResponseView.setTotalPage(coursePage.getTotalPages());
        courseResponseView.setResponses(getAll(search(text,pageable)));
        return courseResponseView;
    }


}
