package com.example.spring_rest_exam.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    STUDENT,INSTRUCTOR,ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
