package org.example.project_base_test;

import org.checkerframework.checker.units.qual.A;
import org.example.project_base_test.service.InitializeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectBaseTestApplication implements CommandLineRunner {
    @Autowired
    private InitializeService initializeService;

    public static void main(String[] args) {
        SpringApplication.run(ProjectBaseTestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        initializeService.initializeData();
    }
}
