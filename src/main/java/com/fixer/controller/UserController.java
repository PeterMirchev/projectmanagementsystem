package com.fixer.controller;

import com.fixer.model.User;
import com.fixer.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader(value = "Authorization", required=false) String token) throws Exception {

        System.out.println();
        User user = userService.findUserProfileByJwt(token);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
