package com.example.spring_rest_exam.dto;

import com.example.spring_rest_exam.StudyFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Enumerated;

@Getter
@Setter
public class StudentRequest {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    @Enumerated
    private StudyFormat studyFormat;
    private Long companyId;
}
