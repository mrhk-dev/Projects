package com.rahmath.springboot.spring_boot_project.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpRequest;
import java.util.Date;

@RestController
public class Home {

    @RequestMapping(value = "/")
    public String sayHello(HttpServletRequest http) {
        return "Hello Rahmath " + http.getSession().getId();
    }

    @RequestMapping(value = "/home")
    public String home1() {
        return "Home Page" + new Date();
    }

    @GetMapping
    public String home(HttpServletRequest http) {
        return "Home Page " + new Date() + "\n     " + http.getSession().getId();
    }
}
