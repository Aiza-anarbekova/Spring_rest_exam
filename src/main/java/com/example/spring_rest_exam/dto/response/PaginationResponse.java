package com.example.spring_rest_exam.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class PaginationResponse {
    private List<CourseResponse> responses;
    private int currentPage;
    private int totalPages;
}
