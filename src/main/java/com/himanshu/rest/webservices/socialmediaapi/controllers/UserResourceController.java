package com.himanshu.rest.webservices.socialmediaapi.controllers;

import com.himanshu.rest.webservices.socialmediaapi.exceptions.UserNotFoundException;
import com.himanshu.rest.webservices.socialmediaapi.models.User;
import com.himanshu.rest.webservices.socialmediaapi.service.UserDaoService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Locale;

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
