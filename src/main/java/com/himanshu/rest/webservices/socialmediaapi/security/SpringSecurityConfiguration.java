package com.himanshu.rest.webservices.socialmediaapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SpringSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        //1 -> All requests must be authenticated
        httpSecurity.authorizeHttpRequests(
                auth -> auth.anyRequest().authenticated()
        );

        //2 -> If request is not authenticated, display a prompt
        httpSecurity.httpBasic(withDefaults());

        //3 -> Disable CSRF <Cross-site Request Forgery> {Allows PUT and POST requests}
        httpSecurity.csrf().disable();

        return httpSecurity.build();
    }

}
