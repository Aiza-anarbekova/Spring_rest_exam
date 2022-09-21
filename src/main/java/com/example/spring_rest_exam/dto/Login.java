package com.example.spring_rest_exam.dto;

import com.example.spring_rest_exam.dto.response.LoginResponse;
import com.example.spring_rest_exam.model.Role;
import com.example.spring_rest_exam.model.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class Login {
    public LoginResponse toLoginVie(String token, String message, User user){
        var loginResponse=  new LoginResponse();
        if (user !=null){
            getAuthority(loginResponse,user.getRoles());
        }
        loginResponse.setMessage(message);
        loginResponse.setJwtToken(token);
        return loginResponse;
    }

    private void getAuthority(LoginResponse loginResponse, List<Role> roles) {
        Set<String > authorities = new HashSet<>();
        for (Role r :roles) {
            authorities.add(r.getName());
        }
        loginResponse.setAuthorities(authorities);
    }
}
