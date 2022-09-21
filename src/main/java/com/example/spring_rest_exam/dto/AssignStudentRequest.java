package com.example.spring_rest_exam.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignStudentRequest {
    private Long courseId;
    private Long studentId;
}
