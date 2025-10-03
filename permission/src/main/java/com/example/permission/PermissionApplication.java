package com.example.permission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PermissionApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(PermissionApplication.class, args);
        System.out.println("123");
    }

}
