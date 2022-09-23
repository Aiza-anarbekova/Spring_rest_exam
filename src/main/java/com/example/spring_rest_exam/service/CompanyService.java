package com.example.spring_rest_exam.service;

import com.example.spring_rest_exam.dto.CompanyRequest;
import com.example.spring_rest_exam.dto.response.CompanyResponse;
import com.example.spring_rest_exam.dto.responseView.CompanyResponseView;
import com.example.spring_rest_exam.exception.NotFoundException;
import com.example.spring_rest_exam.model.Company;
import com.example.spring_rest_exam.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository repository;

    @Autowired
    public CompanyService(CompanyRepository repository) {
        this.repository = repository;
    }

    public CompanyResponse save(CompanyRequest request) {
        Company company = mapToEntity(request);
        return mapToResponse(repository.save(company));
    }

    public Company mapToEntity(CompanyRequest request) {
        return Company.builder()
                .companyName(request.getCompanyName())
                .locatedCountry(request.getLocatedCountry())
                .build();
    }

    public CompanyResponse mapToResponse(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .locatedCountry(company.getLocatedCountry())
                .build();
    }

    public List<CompanyResponse> getAllCompanies() {
        List<CompanyResponse>responses = new ArrayList<>();
        for (Company company:repository.findAll()){
            responses.add(mapToResponse(company));
        }
        return responses;
    }


    public CompanyResponse getById(Long id) {
        Company company = repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("company with id - %s not found!", id)));
        return mapToResponse(company);
    }

    public Company update(Company company, CompanyRequest companyRequest) {
        company.setCompanyName(company.getCompanyName());
        company.setLocatedCountry(companyRequest.getLocatedCountry());
        repository.save(company);
        return company;
    }

    public CompanyResponse updateById(Long id, CompanyRequest request) {
        Company company = repository.findCompanyById(id);
        String companyName = company.getCompanyName();
        String newCompanyName = request.getCompanyName();
        if (newCompanyName != null && !newCompanyName.equals(companyName)) {
            company.setCompanyName(newCompanyName);
        }
        String locatedCountry = company.getLocatedCountry();
        String newLocatedCountry = request.getLocatedCountry();
        if (newLocatedCountry != null && !newLocatedCountry.equals(locatedCountry)) {
            company.setLocatedCountry(newLocatedCountry);
        }
        Company company1 = update(company, request);
        return mapToResponse(repository.save(company1));
    }

    public CompanyResponse deleteCompany(Long id) {
        Company company = repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("company with id - %s not found!", id)));
        repository.delete(company);
        return mapToResponse(company);
    }

    public List<Company> search(String name, Pageable pageable) {
        String text = name == null?"" : name;
        return repository.searchByCompanyName(text.toUpperCase(), pageable);
    }

    public List<CompanyResponse> getAll(List<Company> companies) {
        List<CompanyResponse> responses = new ArrayList<>();
        for (Company c : companies) {
            responses.add(mapToResponse(c));
        }
        return responses;
    }

    public CompanyResponseView pagination(String text, int size, int page) {
        CompanyResponseView companyResponseViews = new CompanyResponseView();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Company> companies = repository.findAll(pageable);
        companyResponseViews.setCurrentPage(pageable.getPageNumber()+1);
        companyResponseViews.setTotalPage(companies.getTotalPages());
        companyResponseViews.setCompanyResponseList(getAll(search(text, pageable)));
        return companyResponseViews;

    }
}