package com.example.spring_rest_exam.dto;

import com.example.spring_rest_exam.validation.EmailValidation;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;

@Getter
@Setter
public class UserRequest {
//    @EmailValidation(message = "this email is already taken!")
    private String email;
    private String password;
    private String name;
}
