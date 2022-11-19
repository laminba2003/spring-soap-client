package com.spring.training.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.callback.KeyStoreCallbackHandler;
import org.springframework.ws.soap.security.wss4j2.support.CryptoFactoryBean;

import java.io.IOException;
import java.util.Map;

@Configuration
@Profile("encrypt")
public class EncryptionSecurityConfig {

    @Bean
    public Wss4jSecurityInterceptor wss4jSecurityInterceptor(ClientConfig clientConfig) throws Exception {
        Wss4jSecurityInterceptor interceptor = new Wss4jSecurityInterceptor();
        Map<String, String> securityConfig = (Map<String, String>) clientConfig.getSecurity().get("certificate");
        interceptor.setSecurementActions("Signature Encrypt");
        interceptor.setValidationActions("Signature Encrypt");
        interceptor.setSecurementUsername(securityConfig.get("alias"));
        interceptor.setSecurementPassword(securityConfig.get("password"));
        interceptor.setSecurementSignatureKeyIdentifier("DirectReference");
        interceptor.setSecurementEncryptionUser(securityConfig.get("alias"));
        CryptoFactoryBean cryptoFactoryBean = cryptoFactoryBean(clientConfig);
        interceptor.setSecurementSignatureCrypto(cryptoFactoryBean.getObject());
        interceptor.setValidationSignatureCrypto(cryptoFactoryBean.getObject());
        interceptor.setSecurementEncryptionCrypto(cryptoFactoryBean.getObject());
        interceptor.setValidationDecryptionCrypto(cryptoFactoryBean.getObject());
        KeyStoreCallbackHandler keyStoreCallbackHandler = new KeyStoreCallbackHandler();
        keyStoreCallbackHandler.setPrivateKeyPassword(securityConfig.get("password"));
        interceptor.setValidationCallbackHandler(keyStoreCallbackHandler);
        return interceptor;
    }

    @Bean
    public CryptoFactoryBean cryptoFactoryBean(ClientConfig clientConfig) throws IOException {
        CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
        Map<String, String> securityConfig = (Map<String, String>) clientConfig.getSecurity().get("certificate");
        DefaultResourceLoader loader = new DefaultResourceLoader();
        cryptoFactoryBean.setKeyStorePassword(securityConfig.get("password"));
        cryptoFactoryBean.setKeyStoreLocation(loader.getResource(securityConfig.get("keyStore")));
        cryptoFactoryBean.setDefaultX509Alias(securityConfig.get("alias"));
        return cryptoFactoryBean;
    }

}
