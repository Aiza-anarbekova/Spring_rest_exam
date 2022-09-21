package com.example.spring_rest_exam.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instructors")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Instructor {
    @Id
    @GeneratedValue(generator = "instructor_gen",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "instructor_gen", sequenceName = "instructor_seq" ,allocationSize = 1)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String email;
    private String specialization;

    public Instructor(String firstName, String lastName, String phoneNumber, String email, String specialization) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.specialization = specialization;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    private Company company;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private List<Course> courses;

    public void addCourse(Course course1) {
        if (courses==null){
            courses=new ArrayList<>();
        }
        courses.add(course1);
    }
}
