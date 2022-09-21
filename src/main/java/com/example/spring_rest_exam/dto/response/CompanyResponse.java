package com.example.spring_rest_exam.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CompanyResponse {
    private Long id;
    private String companyName;
    private String locatedCountry;
}
