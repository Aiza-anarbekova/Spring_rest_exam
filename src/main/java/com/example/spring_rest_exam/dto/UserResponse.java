package com.example.spring_rest_exam.dto;

import com.example.spring_rest_exam.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserResponse {
//private Long id;
    private String email;
    private String token;
    private Role role;



}
