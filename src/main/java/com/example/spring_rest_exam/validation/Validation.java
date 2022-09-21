package com.example.spring_rest_exam.validation;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class Validation {
    public boolean patternMatches(String emailAddress) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9+_-]+(\\.[A-Za-z0-9+_-]+)*@"
                + "gmail.com";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
