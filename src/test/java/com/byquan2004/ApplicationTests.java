package com.byquan2004;

import com.byquan2004.config.OpenAPIConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private OpenAPIConfig openAPIConfig;
    @Test
    void contextLoads() {
        System.out.println(openAPIConfig.getUrl1());
    }

}
