package com.himanshu.rest.webservices.socialmediaapi.repositories;

import com.himanshu.rest.webservices.socialmediaapi.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {

}
