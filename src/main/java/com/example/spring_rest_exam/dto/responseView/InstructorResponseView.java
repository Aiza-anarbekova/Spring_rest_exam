package com.example.spring_rest_exam.dto.responseView;

import com.example.spring_rest_exam.dto.response.InstructorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class InstructorResponseView {
    private List<InstructorResponse> responses;
    private int currentPage;
    private int totalPage;
}
