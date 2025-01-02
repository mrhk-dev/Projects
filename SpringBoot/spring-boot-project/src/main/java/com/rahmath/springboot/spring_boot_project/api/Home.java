package com.rahmath.springboot.spring_boot_project.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class Home {

    @RequestMapping(value = "/")
    public String sayHello() {
        return "Hello Rahmath";
    }

    @RequestMapping(value = "/home")
    public String home1() {
        return "Home Page" + new Date();
    }

    @GetMapping
    public String home(){
        return "Home Page " + new Date();
    }
}
