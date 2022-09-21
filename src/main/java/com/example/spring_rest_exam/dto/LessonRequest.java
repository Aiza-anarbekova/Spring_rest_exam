package com.example.spring_rest_exam.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonRequest {
    private String lessonName;
    private Long courseId;
}
