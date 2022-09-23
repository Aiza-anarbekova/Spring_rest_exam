package com.example.spring_rest_exam.dto;

import com.example.spring_rest_exam.model.enums.Role;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InstructorRequest {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private String specialization;
    private Long companyId;
}
