package com.friends.test.automation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.io.IOException;

@SpringBootApplication
@PropertySources(value = {@PropertySource(value = "application.properties"),
        @PropertySource(value = "application-persistent.properties")})
// @RequiredArgsConstructor
public class Application {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
    }

}
