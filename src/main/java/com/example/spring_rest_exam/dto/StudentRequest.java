package com.example.spring_rest_exam.dto;

import com.example.spring_rest_exam.StudyFormat;
import com.example.spring_rest_exam.model.enums.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class StudentRequest {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private StudyFormat studyFormat;
    private Long companyId;
    private String email;
    private String password;
}
