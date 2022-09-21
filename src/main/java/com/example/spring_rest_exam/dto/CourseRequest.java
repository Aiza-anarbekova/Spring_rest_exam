package com.example.spring_rest_exam.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;

@Getter
@Setter
public class CourseRequest {

    private String name;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfStart;
    private String duration;
    private String description;
    private Long companyId;
}
