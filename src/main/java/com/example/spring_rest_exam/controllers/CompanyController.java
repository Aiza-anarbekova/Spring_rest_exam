package com.example.spring_rest_exam.controllers;

import com.example.spring_rest_exam.dto.CompanyRequest;
import com.example.spring_rest_exam.dto.response.CompanyResponse;
import com.example.spring_rest_exam.dto.responseView.CompanyResponseView;
import com.example.spring_rest_exam.model.Company;
import com.example.spring_rest_exam.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/company")
@PreAuthorize("hasAuthority('MANAGER')")
@Tag(name = "company api",description = "MANAGER can create ")
public class CompanyController {
    private final CompanyService service;

    @Autowired
    public CompanyController(CompanyService companyRepository) {
        this.service = companyRepository;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(description = "ADMIN can get by id")
    public CompanyResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public CompanyResponse saveCompany(@RequestBody CompanyRequest request) {
        return service.save(request);
    }


    @PatchMapping("/{id}")
    public CompanyResponse updateCompany(@RequestBody CompanyRequest request,
                                         @PathVariable Long id) {
        return service.updateById(id, request);
    }

    @DeleteMapping("/{id}")
    public CompanyResponse deleteCompany(@PathVariable("id") Long id) {
        return service.deleteCompany(id);
    }

    @GetMapping
    public CompanyResponseView pagination(@RequestParam(name = "text",required = false) String text,
                                          @RequestParam int size,
                                          @RequestParam int page){
        return service.pagination(text,size,page);
    }

    @GetMapping("/all")
    public List<Company> findAll(){
        return service.getAllCompanies();
    }
}
