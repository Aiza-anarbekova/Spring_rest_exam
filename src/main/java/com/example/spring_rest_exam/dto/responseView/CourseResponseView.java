package com.example.spring_rest_exam.dto.responseView;

import com.example.spring_rest_exam.dto.response.CourseResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class CourseResponseView {
    List<CourseResponse> responses;
    private int currentPage;
    private int totalPage;
}
