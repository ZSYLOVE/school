package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/h2")
public class HelloWorldController {
    @GetMapping("/h1")
    public String hello()
    {
        return "hello world";
        }
}