package com.example.spring_rest_exam.dto.responseView;

import com.example.spring_rest_exam.dto.response.TaskResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Component
public class TaskResponseView {
    private List<TaskResponse> responses;

}
