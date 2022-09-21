package com.example.spring_rest_exam.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidateImpl.class)
public @interface EmailValidation {
    String message() default "invalid email";

    Class<?>[] groups () default {};

    Class<? extends Payload>[] payload() default {};
}
