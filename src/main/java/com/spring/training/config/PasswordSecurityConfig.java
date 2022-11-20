package com.spring.training.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;
import org.springframework.ws.soap.security.xwss.callback.SimplePasswordValidationCallbackHandler;

import java.util.Map;

@Configuration
@Profile("password")
@AllArgsConstructor
public class PasswordSecurityConfig {

    final ClientConfig clientConfig;

    @Bean
    public XwsSecurityInterceptor securityInterceptor() {
        XwsSecurityInterceptor interceptor = new XwsSecurityInterceptor();
        Map<String, String> securityConfig = (Map<String, String>) clientConfig.getSecurity().get("password");
        DefaultResourceLoader loader = new DefaultResourceLoader();
        interceptor.setPolicyConfiguration(loader.getResource(securityConfig.get("policy")));
        interceptor.setCallbackHandler(new SimplePasswordValidationCallbackHandler());
        return interceptor;
    }

}
