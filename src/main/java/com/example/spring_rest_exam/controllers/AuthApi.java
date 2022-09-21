package com.example.spring_rest_exam.controllers;

import com.example.spring_rest_exam.dto.Login;
import com.example.spring_rest_exam.dto.UserRequest;
import com.example.spring_rest_exam.dto.UserResponse;
import com.example.spring_rest_exam.dto.response.LoginResponse;
import com.example.spring_rest_exam.model.User;
import com.example.spring_rest_exam.repository.UserRepository;
import com.example.spring_rest_exam.security.jwt.JwtTokenUtil;
import com.example.spring_rest_exam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jwt")
public class AuthApi {
    private final UserService userService;
    private final UserRepository repository;
    private final JwtTokenUtil jwtTokenUtil;
    private final Login login;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> getLogin(@RequestBody @Valid UserRequest request){
        try {
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(request.getEmail(),
                    request.getPassword());
            authenticationManager.authenticate(token);
            User user = repository.findByEmail(token.getName()).get();
            return ResponseEntity.ok().body(login.toLoginVie
                    (jwtTokenUtil.generateToken(user),"successful",user));
        }catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    login.toLoginVie("","login failed",null));
        }
    }

    @PostMapping("/reg")
    public UserResponse create(@RequestBody @Valid UserRequest request){
        return userService.create(request);
    }

}
