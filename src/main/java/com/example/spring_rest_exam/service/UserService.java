package com.example.spring_rest_exam.service;

import com.example.spring_rest_exam.dto.UserRequest;
import com.example.spring_rest_exam.dto.UserResponse;
import com.example.spring_rest_exam.exception.BadRequestException;
import com.example.spring_rest_exam.model.User;
import com.example.spring_rest_exam.repository.UserRepository;
import com.example.spring_rest_exam.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Validation validation;

    public UserResponse create(UserRequest userRequest) {
//        String email = userRequest.getEmail();
//        if (!validation.patternMatches(email)) {
//            throw new BadRequestException(email + " is not valid");
//
//        }
//        checkEmail(email);
//        boolean exists = repository.existsByEmail(userRequest.getEmail());
        User user = mapToEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return mapToResponse(repository.save(user));
    }

    public User mapToEntity(UserRequest request) {
   return User.builder()
//           .username(request.getName())
           .password(request.getPassword())
           .email(request.getEmail())
           .createdDate(LocalDate.now())
           .isActive(true)
           .build();

    }

    public UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .createdDate(LocalDate.now())
                .username(user.getUsername())
                .isActive(true)
                .build();
    }

    private void checkEmail(String email) {
        boolean exists = repository.existsByEmail(email);
        if (exists) {
            throw new BadRequestException(
                    "Instructor with email = " + email + " already exists"
            );
        }
    }

}
