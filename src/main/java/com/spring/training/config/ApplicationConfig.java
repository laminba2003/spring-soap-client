package com.spring.training.config;

import com.spring.training.client.CountryClient;
import com.spring.training.client.PersonClient;
import com.spring.training.exception.SoapExceptionResolver;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import java.util.Properties;

@Configuration
@AllArgsConstructor
public class ApplicationConfig {

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
    public CountryClient countryClient(ClientConfig clientConfig, ClientInterceptor[] interceptors) {
        return new CountryClient(clientConfig, interceptors);
    }

    @Bean
    public PersonClient personClient(ClientConfig clientConfig, ClientInterceptor[] interceptors) {
        return new PersonClient(clientConfig, interceptors);
    }

}