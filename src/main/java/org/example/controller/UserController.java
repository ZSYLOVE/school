package org.example.controller;


import cn.dev33.satoken.util.SaResult;
import lombok.RequiredArgsConstructor;
import org.example.entitys.LoginUser;
import org.example.servise.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public SaResult login(@RequestBody LoginUser loginUser) {
        return userService.login(loginUser);
    }
}