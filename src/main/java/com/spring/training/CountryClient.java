package com.spring.training;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import com.example.consumingwebservice.wsdl.GetCountryRequest;
import com.example.consumingwebservice.wsdl.GetCountryResponse;

@AllArgsConstructor
public class CountryClient extends WebServiceGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(CountryClient.class);
    private ClientConfiguration clientConfiguration;

    public GetCountryResponse getCountry(String country) {

        GetCountryRequest request = new GetCountryRequest();
        request.setName(country);

        log.info("Requesting location for " + country);

        GetCountryResponse response = (GetCountryResponse) getWebServiceTemplate()
                .marshalSendAndReceive(clientConfiguration.getDefaultUri(), request,
                        new SoapActionCallback(
                                "http://spring.io/guides/gs-producing-web-service/GetCountryRequest"));

        return response;
    }

}