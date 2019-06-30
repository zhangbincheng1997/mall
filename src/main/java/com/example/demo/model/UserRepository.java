package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

//    @Query("select u from User u where u.id = ?1")
//    User findById(long id);

}
