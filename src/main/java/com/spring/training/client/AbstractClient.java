package com.spring.training.client;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public abstract class AbstractClient extends WebServiceGatewaySupport {

    public <T, U> U sendRequest (T request, Class<U> clazz) {
        return clazz.cast(getWebServiceTemplate().marshalSendAndReceive(request));
    }

}
