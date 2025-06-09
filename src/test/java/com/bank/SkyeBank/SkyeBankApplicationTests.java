package com.bank.SkyeBank;

import com.bank.DotenvApplicationContextInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = DotenvApplicationContextInitializer.class)
public class SkyeBankApplicationTests {

    @Test
    void contextLoads() {
    }
}
