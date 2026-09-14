package com.evaluacion.operaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ApiOperacionesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiOperacionesApplication.class, args);
    }
}
