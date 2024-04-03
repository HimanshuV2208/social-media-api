package com.himanshu.rest.webservices.socialmediaapi.controllers;

import com.himanshu.rest.webservices.socialmediaapi.exceptions.UserNotFoundException;
import com.himanshu.rest.webservices.socialmediaapi.models.User;
import com.himanshu.rest.webservices.socialmediaapi.repositories.UserRepository;
import com.himanshu.rest.webservices.socialmediaapi.service.UserDaoService;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserJpaResourceController {
    private UserRepository userRepository;

    public UserJpaResourceController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/jpa/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable Integer id) {
        Optional<User> retrievedUser = userRepository.findById(id);
        if (retrievedUser.isEmpty()) throw new UserNotFoundException("ID : " + id);

        EntityModel<User> userEntityModel = EntityModel.of(retrievedUser.get());
        WebMvcLinkBuilder linkBuilder = linkTo(methodOn(this.getClass()).retrieveAllUsers());

        userEntityModel.add(linkBuilder.withRel("all-users"));

        return userEntityModel;
    }

    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }

    @PostMapping("/jpa/users")
    public ResponseEntity<Object> addUser(@Valid @RequestBody User user) {
        User createdUser = userRepository.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

}
