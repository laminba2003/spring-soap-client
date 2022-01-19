package com.spring.training;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@AllArgsConstructor
public class ApplicationConfiguration {

    private static final String CONTEXT_PATH = "com.example.consumingwebservice.wsdl";

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(CONTEXT_PATH);
        return marshaller;
    }

    @Bean
    public CountryClient countryClient(Jaxb2Marshaller marshaller, ClientConfiguration clientConfiguration) {
        CountryClient client = new CountryClient(clientConfiguration);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }

}