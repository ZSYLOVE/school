package org.example.controller;


import cn.dev33.satoken.util.SaResult;
import lombok.RequiredArgsConstructor;
import org.example.comment.Valida;
import org.example.entitys.LoginUser;
import org.example.entitys.User;
import org.example.mapper.UserMapper;
import org.example.servise.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public SaResult login(@RequestBody LoginUser loginUser) {
        return userService.login(loginUser);
    }
    @GetMapping("/logout")
    public SaResult logout() {
        return userService.logout();

    }
    @GetMapping("/id")
    public SaResult selectUser(Long id) {
        return userService.selectUser(id);
    }

    @PostMapping("/update")
    public SaResult update(@Validated(Valida.Update.class) @RequestBody User user) {
        return userService.updateUser(user);
    }
    @PostMapping("/saveUser")
    public SaResult save(@Validated (Valida.Create.class)@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/delete")
    public SaResult delete(Long id) {
        return userService.deletebyid(id);
    }

    @GetMapping("/page")
    public  SaResult selectPage(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, Map<String, Object> search) {
        return userService.pageUser(currentPage,pageSize,search);

    }
}