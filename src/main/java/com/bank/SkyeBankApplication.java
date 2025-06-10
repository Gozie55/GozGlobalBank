package com.bank;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SkyeBankApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SkyeBankApplication.class);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(SkyeBankApplication.class)
            .initializers(new DotenvApplicationContextInitializer()) // Load .env here
            .run(args);
    }
}