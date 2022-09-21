package com.example.spring_rest_exam.dto.response;

import com.example.spring_rest_exam.StudyFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Enumerated;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class StudentResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    @Enumerated
    private StudyFormat studyFormat;
}
