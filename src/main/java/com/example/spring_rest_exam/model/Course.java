package com.example.spring_rest_exam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(generator = "course_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "course_gen", sequenceName = "course_seq", allocationSize = 1)
    private Long id;
    @Column(name = "course_name")
    private String name;
    @Column(name = "date_of_start")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfStart;
    private String duration;
    @Column(length = 10000)
    private String image;
    private String description;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH},
            mappedBy = "courses",
            fetch = FetchType.LAZY)
    private List<Instructor> instructors;

    public void addInstructor(Instructor instructor1) {
        if (instructors == null) {
            instructors = new ArrayList<>();
        } else {

            this.instructors.add(instructor1);
        }
    }

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course", fetch = FetchType.LAZY)
    private List<Student> students;

    public void addStudent(Student student1) {
        students.add(student1);
    }

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private List<Lesson> lessons = new ArrayList<>();

    public void addLesson(Lesson lesson1) {
        lessons.add(lesson1);
    }

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Company company;


}
