package org.example.hackatonapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class HackatonApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HackatonApiApplication.class, args);
    }

}
