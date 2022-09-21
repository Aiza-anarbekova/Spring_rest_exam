package com.example.spring_rest_exam.repository;

import com.example.spring_rest_exam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
   @Query("select u from  User  u where u.email = :email")
   Optional< User> findByEmail(String email);

   @Query("select case when count(a)>0 then true else false end" +
           " from User a where a.email =?1")
   boolean existsByEmail(String email);
}
