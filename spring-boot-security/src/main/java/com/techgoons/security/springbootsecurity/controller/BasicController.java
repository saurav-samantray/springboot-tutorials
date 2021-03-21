package com.techgoons.security.springbootsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

    @GetMapping("/")
    public String hello(){
        return "Hello Public";
    }

    @GetMapping("/user")
    public String helloPublic(){
        return "Hello User";
    }

    @GetMapping("/admin")
    public String helloPrivate(){
        return "Hello Admin";
    }
}
