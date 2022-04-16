package com.example.demo.controller;

import com.example.demo.model.entity.User;
import com.example.demo.model.request.UserRegistrationRequest;
import com.example.demo.service.impl.UserService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{value}")
    public User getUser(@PathVariable(name = "value") String value) {
        return userService.retrieveUser(value);
    }

    @PostMapping
    public User postRegisterUser(@RequestBody @Validated UserRegistrationRequest request) {
        return userService.saveUser(request);
    }

}
