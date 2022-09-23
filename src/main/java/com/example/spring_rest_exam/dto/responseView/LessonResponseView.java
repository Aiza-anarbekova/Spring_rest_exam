package com.example.spring_rest_exam.dto.responseView;

import com.example.spring_rest_exam.dto.response.LessonResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@NoArgsConstructor
public class LessonResponseView {
    private List<LessonResponse> responseList;
    private int currentPage;
    private int totalPage;
}
