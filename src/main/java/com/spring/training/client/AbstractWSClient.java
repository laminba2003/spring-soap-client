package com.spring.training.client;

import com.spring.training.config.ClientConfig;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;

public abstract class AbstractWSClient extends WebServiceGatewaySupport {

    public AbstractWSClient(ClientConfig clientConfig, XwsSecurityInterceptor securityInterceptor) {
        setDefaultUri(clientConfig.getLocation());
        setInterceptors(new ClientInterceptor[]{securityInterceptor});
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.spring.training.model");
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);
    }

    public <T, U> U sendRequest(T request, Class<U> clazz) {
        return clazz.cast(getWebServiceTemplate().marshalSendAndReceive(request));
    }

}
