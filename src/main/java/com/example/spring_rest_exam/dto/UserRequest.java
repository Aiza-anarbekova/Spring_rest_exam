package com.example.spring_rest_exam.dto;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserRequest {

    private String email;
    private String password;

}
