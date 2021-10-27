package com.millenium.milleniumserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MilleniumServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MilleniumServerApplication.class, args);
    }

}
