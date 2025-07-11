package com.example.githubapi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;

//@ComponentScan(basePackages = "com.example.githubapi")
@SpringBootApplication
public class GithubEventsApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(GithubEventsApiApplication.class, args);
    }
}