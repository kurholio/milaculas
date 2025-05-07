package com.milaculas;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MilaculasController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from backend!";
    }
    
    @GetMapping("/catalog")
    public String catalog() {
        return "Hello from catalogsssssssssssssssssssssssssssssssssssssss!";
    }
}