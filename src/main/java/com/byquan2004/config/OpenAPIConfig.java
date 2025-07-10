package com.byquan2004.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("free-api")
public class OpenAPIConfig {
    private String url1;
    private String url2;
    private String url3;
    private String url4;
    private String url5;
    private String url6;
    private String url7;
}
