package com.himanshu.rest.webservices.socialmediaapi.controllers;

import com.himanshu.rest.webservices.socialmediaapi.exceptions.UserNotFoundException;
import com.himanshu.rest.webservices.socialmediaapi.models.User;
import com.himanshu.rest.webservices.socialmediaapi.service.UserDaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserResourceController {
    private UserDaoService userDaoService;

    public UserResourceController(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return userDaoService.findAll();
    }

    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable Integer id) {
        User retrievedUser = userDaoService.findOneById(id);
        if(retrievedUser == null) throw new UserNotFoundException("ID : " + id);
        return retrievedUser;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userDaoService.deleteById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<Object> addUser(@RequestBody User user) {
        User createdUser = userDaoService.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

}
