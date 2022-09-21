package com.example.spring_rest_exam.repository;

import com.example.spring_rest_exam.model.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CompanyRepository extends JpaRepository<Company , Long> {
    Company findCompanyById(Long id);

    @Query("select c from Company c where upper(c.companyName) like concat('%',:text,'%')")
    List<Company> searchByCompanyName(String text, Pageable pageable);
}
