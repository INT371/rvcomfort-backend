package sit.it.rvcomfort.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sit.it.rvcomfort.model.entity.User;
import sit.it.rvcomfort.model.request.UserRegistrationRequest;
import sit.it.rvcomfort.service.impl.UserService;

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
