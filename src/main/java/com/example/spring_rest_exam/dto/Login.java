package com.example.spring_rest_exam.dto;

import com.example.spring_rest_exam.dto.response.LoginResponse;

import com.example.spring_rest_exam.model.User;
import com.example.spring_rest_exam.model.enums.Role;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class Login {
    public LoginResponse toLoginVie(String token, String message, User user){
        var loginResponse=  new LoginResponse();
        if (user !=null){
            getAuthority(loginResponse,user.getRole());
        }
        loginResponse.setMessage(message);
        loginResponse.setJwtToken(token);
        return loginResponse;
    }

    private void getAuthority(LoginResponse loginResponse, Role roles) {
        Set<String > authorities = new HashSet<>();
       authorities.add(roles.getAuthority());
        loginResponse.setAuthorities(authorities);
    }
}
