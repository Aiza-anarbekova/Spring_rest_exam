package com.example.spring_rest_exam.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private LocalDate createdDate;
    private Boolean isActive=true;
}
