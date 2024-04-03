package com.himanshu.rest.webservices.socialmediaapi.repositories;

import com.himanshu.rest.webservices.socialmediaapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
