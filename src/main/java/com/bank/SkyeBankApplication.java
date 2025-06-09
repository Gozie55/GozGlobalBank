package com.bank;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SkyeBankApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SkyeBankApplication.class)
            .initializers(new DotenvApplicationContextInitializer()) // ✅ Load .env here
            .run(args);
    }
}
