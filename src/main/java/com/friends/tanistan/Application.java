package com.friends.tanistan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
@PropertySources(value = {@PropertySource(value = "application.properties"),
        @PropertySource(value = "application-persistent.properties")})
// @RequiredArgsConstructor
public class Application {

    private static Process p;

    public static void main(String[] args) throws IOException {
        launchChromeDriver();
        SpringApplication.run(Application.class, args);
    }

    private static void launchChromeDriver() throws IOException {
        File f = new File("");
        System.out.println(f.getAbsolutePath());
        String[] cmd = {f.getAbsolutePath() + "\\driver\\chromedriver.exe"};
        p = Runtime.getRuntime().exec(cmd);
        if(p.isAlive()) {
            System.out.println("Chrome driver is alive");
        }
    }

    @PreDestroy
    public void destory() {
        p.destroyForcibly();
        System.out.println("Chrome driver is destroyed");
    }
}
