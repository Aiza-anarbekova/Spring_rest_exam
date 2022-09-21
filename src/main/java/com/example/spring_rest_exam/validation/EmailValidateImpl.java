package com.example.spring_rest_exam.validation;

import com.example.spring_rest_exam.model.User;
import com.example.spring_rest_exam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;
import java.util.regex.Pattern;

public class EmailValidateImpl implements ConstraintValidator<EmailValidation,String> {
    private final UserRepository repository;

    @Autowired
    public EmailValidateImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> emailAddress = repository.findByEmail(email);
        if (emailAddress.isPresent()){
            return false;
        }
        return true;
    }
}
