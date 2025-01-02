package com.rahmath.springboot.spring_boot_project.filers;


import jakarta.servlet.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class filter_1 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Filter 1 called");

        chain.doFilter(request,response);
    }
}

