package com.himanshu.rest.webservices.socialmediaapi.controllers;

import com.himanshu.rest.webservices.socialmediaapi.exceptions.UserNotFoundException;
import com.himanshu.rest.webservices.socialmediaapi.models.Post;
import com.himanshu.rest.webservices.socialmediaapi.models.User;
import com.himanshu.rest.webservices.socialmediaapi.repositories.PostRepository;
import com.himanshu.rest.webservices.socialmediaapi.repositories.UserRepository;
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
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserJpaResourceController(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
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

    @GetMapping("/jpa/users/{id}/posts")
    public List<Post> retrievePostsForUser(@PathVariable Integer id) {
        Optional<User> retrievedUser = userRepository.findById(id);
        if (retrievedUser.isEmpty()) throw new UserNotFoundException("ID : " + id);

        return retrievedUser.get().getPosts();
    }

    @PostMapping("/jpa/users/{id}/posts")
    public ResponseEntity<Object> addPostForUser(@Valid @RequestBody Post post, @PathVariable Integer id) {
        Optional<User> retrievedUser = userRepository.findById(id);
        if (retrievedUser.isEmpty()) throw new UserNotFoundException("ID : " + id);

        post.setUser(retrievedUser.get());
        Post createdPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPost.getId())
                .toUri();
        return ResponseEntity.created(location).build();

    }

}
