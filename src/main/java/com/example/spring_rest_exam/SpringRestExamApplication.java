package com.example.spring_rest_exam;

import com.example.spring_rest_exam.model.Student;
import com.example.spring_rest_exam.model.User;
import com.example.spring_rest_exam.model.enums.Role;
import com.example.spring_rest_exam.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringRestExamApplication {
    private final StudentRepository repository;

    public SpringRestExamApplication(StudentRepository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringRestExamApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLiner(){
//        return (args)->{
//            if (repository.findStudentByUserEmail("admin@gmail.com")==null) {
//                User authInfo = new User();
//                Student student = new Student("Admin", "Admin","099988765", authInfo);
//                repository.save(student);
//            }
//        };
//    }

}
