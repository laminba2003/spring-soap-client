package com.spring.training.config;

import com.spring.training.client.CountryClient;
import com.spring.training.client.PersonClient;
import com.spring.training.exception.SoapExceptionResolver;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.ws.soap.security.support.KeyStoreFactoryBean;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;
import org.springframework.ws.soap.security.xwss.callback.KeyStoreCallbackHandler;
import org.springframework.ws.soap.security.xwss.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import java.util.Map;
import java.util.Properties;

@Configuration
@AllArgsConstructor
public class ApplicationConfig {

    @Bean
    @Profile("password")
    public XwsSecurityInterceptor passwordSecurityInterceptor(ClientConfig clientConfig) {
        XwsSecurityInterceptor interceptor = new XwsSecurityInterceptor();
        Map<String, String> securityConfig = (Map<String, String>) clientConfig.getSecurity().get("password");
        DefaultResourceLoader loader = new DefaultResourceLoader();
        interceptor.setPolicyConfiguration(loader.getResource(securityConfig.get("policy")));
        interceptor.setCallbackHandler(new SimplePasswordValidationCallbackHandler());
        return interceptor;
    }

    @Bean
    @Profile("certificate")
    public XwsSecurityInterceptor certificateSecurityInterceptor(ClientConfig clientConfig) {
        XwsSecurityInterceptor interceptor = new XwsSecurityInterceptor();
        Map<String, String> securityConfig = (Map<String, String>) clientConfig.getSecurity().get("certificate");
        DefaultResourceLoader loader = new DefaultResourceLoader();
        interceptor.setPolicyConfiguration(loader.getResource(securityConfig.get("policy")));
        KeyStoreCallbackHandler handler = new KeyStoreCallbackHandler();
        handler.setDefaultAlias(securityConfig.get("alias"));
        KeyStoreFactoryBean keyStoreFactoryBean = keyStoreFactoryBean(clientConfig);
        handler.setTrustStore(keyStoreFactoryBean.getObject());
        handler.setKeyStore(keyStoreFactoryBean.getObject());
        handler.setPrivateKeyPassword(securityConfig.get("password"));
        interceptor.setCallbackHandler(handler);
        return interceptor;
    }

    @Bean
    public KeyStoreFactoryBean keyStoreFactoryBean(ClientConfig clientConfig) {
        Map<String, String> securityConfig = (Map<String, String>) clientConfig.getSecurity().get("certificate");
        DefaultResourceLoader loader = new DefaultResourceLoader();
        KeyStoreFactoryBean keyStoreFactoryBean = new KeyStoreFactoryBean();
        keyStoreFactoryBean.setType("JKS");
        keyStoreFactoryBean.setLocation(loader.getResource(securityConfig.get("trustStore")));
        keyStoreFactoryBean.setPassword(securityConfig.get("password"));
        return keyStoreFactoryBean;
    }

    @Bean
    public SoapFaultMappingExceptionResolver exceptionResolver() {
        SoapFaultMappingExceptionResolver exceptionResolver = new SoapExceptionResolver();
        SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
        faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
        exceptionResolver.setDefaultFault(faultDefinition);
        Properties errorMappings = new Properties();
        errorMappings.setProperty(Exception.class.getName(), SoapFaultDefinition.SERVER.toString());
        exceptionResolver.setExceptionMappings(errorMappings);
        exceptionResolver.setOrder(1);
        return exceptionResolver;
    }

    @Bean
    @ConfigurationProperties(prefix = "ws")
    public ClientConfig clientConfig() {
        return new ClientConfig();
    }

    @Bean
    public CountryClient countryClient(ClientConfig clientConfig, XwsSecurityInterceptor securityInterceptor) {
        return new CountryClient(clientConfig, securityInterceptor);
    }

    @Bean
    public PersonClient personClient(ClientConfig clientConfig, XwsSecurityInterceptor securityInterceptor) {
        return new PersonClient(clientConfig, securityInterceptor);
    }

}