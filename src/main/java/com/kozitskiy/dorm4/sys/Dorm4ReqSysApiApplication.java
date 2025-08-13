package com.kozitskiy.dorm4.sys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Dorm4ReqSysApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(Dorm4ReqSysApiApplication.class, args);
    }

}
