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
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.support.KeyStoreFactoryBean;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.support.CryptoFactoryBean;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;
import org.springframework.ws.soap.security.xwss.callback.KeyStoreCallbackHandler;
import org.springframework.ws.soap.security.xwss.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import java.io.IOException;
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
    @Profile("certificate")
    public KeyStoreFactoryBean keyStoreFactoryBean(ClientConfig clientConfig) {
        Map<String, String> securityConfig = (Map<String, String>) clientConfig.getSecurity().get("certificate");
        DefaultResourceLoader loader = new DefaultResourceLoader();
        KeyStoreFactoryBean keyStoreFactoryBean = new KeyStoreFactoryBean();
        keyStoreFactoryBean.setType("JKS");
        keyStoreFactoryBean.setLocation(loader.getResource(securityConfig.get("keyStore")));
        keyStoreFactoryBean.setPassword(securityConfig.get("password"));
        return keyStoreFactoryBean;
    }

    @Bean
    @Profile("encrypt")
    public Wss4jSecurityInterceptor wss4jSecurityInterceptor(ClientConfig clientConfig) throws Exception {
        Wss4jSecurityInterceptor interceptor = new Wss4jSecurityInterceptor();
        Map<String, String> securityConfig = (Map<String, String>) clientConfig.getSecurity().get("certificate");
        interceptor.setSecurementActions("Signature");
        interceptor.setValidationActions("Signature");
        interceptor.setSecurementUsername(securityConfig.get("alias"));
        interceptor.setSecurementPassword(securityConfig.get("password"));
        interceptor.setSecurementSignatureKeyIdentifier("DirectReference");
        CryptoFactoryBean cryptoFactoryBean = cryptoFactoryBean(clientConfig);
        interceptor.setSecurementSignatureCrypto(cryptoFactoryBean.getObject());
        interceptor.setValidationSignatureCrypto(cryptoFactoryBean.getObject());
        return interceptor;
    }

    @Bean
    @Profile("encrypt")
    public CryptoFactoryBean cryptoFactoryBean(ClientConfig clientConfig) throws IOException {
        CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
        Map<String, String> securityConfig = (Map<String, String>) clientConfig.getSecurity().get("certificate");
        DefaultResourceLoader loader = new DefaultResourceLoader();
        cryptoFactoryBean.setKeyStorePassword(securityConfig.get("password"));
        cryptoFactoryBean.setKeyStoreLocation(loader.getResource(securityConfig.get("keyStore")));
        cryptoFactoryBean.setDefaultX509Alias("thinktech");
        return cryptoFactoryBean;
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
    public CountryClient countryClient(ClientConfig clientConfig, ClientInterceptor[] interceptors) {
        return new CountryClient(clientConfig, interceptors);
    }

    @Bean
    public PersonClient personClient(ClientConfig clientConfig, ClientInterceptor[] interceptors) {
        return new PersonClient(clientConfig, interceptors);
    }

}