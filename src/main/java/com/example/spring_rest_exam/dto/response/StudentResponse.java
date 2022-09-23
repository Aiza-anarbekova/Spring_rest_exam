package com.example.spring_rest_exam.dto.response;

import com.example.spring_rest_exam.StudyFormat;
import lombok.*;

import javax.persistence.Enumerated;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    @Enumerated
    private StudyFormat studyFormat;


}
