package com.himanshu.rest.webservices.socialmediaapi.controllers;

import com.himanshu.rest.webservices.socialmediaapi.exceptions.UserNotFoundException;
import com.himanshu.rest.webservices.socialmediaapi.models.User;
import com.himanshu.rest.webservices.socialmediaapi.service.UserDaoService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserResourceController {
    private UserDaoService userDaoService;
    private MessageSource messageSource;


    public UserResourceController(UserDaoService userDaoService, MessageSource messageSource) {
        this.userDaoService = userDaoService;
        this.messageSource = messageSource;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return userDaoService.findAll();
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable Integer id) {
        User retrievedUser = userDaoService.findOneById(id);
        if(retrievedUser == null) throw new UserNotFoundException("ID : " + id);

        EntityModel<User> userEntityModel = EntityModel.of(retrievedUser);
        WebMvcLinkBuilder linkBuilder = linkTo(methodOn(this.getClass()).retrieveAllUsers());

        userEntityModel.add(linkBuilder.withRel("all-users"));

        return userEntityModel;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userDaoService.deleteById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<Object> addUser(@Valid @RequestBody User user) {
        User createdUser = userDaoService.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/greet-internationalized")
    public String helloWorldInternationalized() {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage("good.morning.message", null, "Default", locale);
    }

}
