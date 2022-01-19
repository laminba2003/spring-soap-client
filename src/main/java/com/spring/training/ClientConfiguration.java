package com.spring.training;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "soap")
@Data
public class ClientConfiguration {
    private String defaultUri;
}
