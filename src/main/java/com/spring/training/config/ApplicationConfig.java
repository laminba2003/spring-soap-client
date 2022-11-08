package com.spring.training.config;

import com.spring.training.client.CountryClient;
import com.spring.training.client.PersonClient;
import com.spring.training.exception.SoapExceptionResolver;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
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
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.spring.training.model");
        return marshaller;
    }

    @Bean
    public CountryClient countryClient(Jaxb2Marshaller marshaller, ClientConfig clientConfig) {
        CountryClient client = new CountryClient();
        client.setDefaultUri(clientConfig.getDefaultUri());
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }

    @Bean
    public PersonClient personClient(Jaxb2Marshaller marshaller, ClientConfig clientConfig) {
        PersonClient client = new PersonClient();
        client.setDefaultUri(clientConfig.getDefaultUri());
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }

    @Bean
    @ConfigurationProperties(prefix = "services")
    public ClientConfig clientConfig() {
        return new ClientConfig();
    }

}