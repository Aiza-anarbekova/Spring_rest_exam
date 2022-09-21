package com.example.spring_rest_exam.dto.responseView;

import com.example.spring_rest_exam.dto.response.CompanyResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Getter
@Setter
public class CompanyResponseView {
    private List<CompanyResponse> companyResponseList;
}
