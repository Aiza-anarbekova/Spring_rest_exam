package com.example.spring_rest_exam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    @Id
    @GeneratedValue(generator = "company_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "company_seq",sequenceName = "company_seq", allocationSize = 1)
    private Long id;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "located_country")
    private String locatedCountry;

    @JsonIgnore
    @OneToMany(cascade ={CascadeType.MERGE, CascadeType.REMOVE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "company")
    private List<Course> course;

    public void addCourse(Course course1) {
        if (course == null) {
            course = new ArrayList<>();
        }
        course.add(course1);
    }

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "company")
    private List<Student> students;

    public void addStudent(Student student1) {
        students.add(student1);
    }

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private List<Instructor> instructors;

    public void addInstructor(Instructor instructor1) {
        if (instructors == null) {
            instructors = new ArrayList<>();
        }
        instructors.add(instructor1);
    }
}
