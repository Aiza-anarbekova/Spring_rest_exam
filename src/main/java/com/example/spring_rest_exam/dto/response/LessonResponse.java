package com.example.spring_rest_exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LessonResponse {
    private Long id;
    private String lessonName;
}
