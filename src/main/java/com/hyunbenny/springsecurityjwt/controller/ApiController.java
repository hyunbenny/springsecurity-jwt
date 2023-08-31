package com.hyunbenny.springsecurityjwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/home")
    public String home() {
        return "<h1>HOME</h1>";
    }

    @PostMapping("/token")
    public String token() {
        return "hello world";
    }

}
