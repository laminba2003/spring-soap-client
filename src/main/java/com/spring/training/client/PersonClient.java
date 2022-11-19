package com.spring.training.client;

import com.spring.training.config.ClientConfig;
import com.spring.training.model.*;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

import java.util.List;

public class PersonClient extends AbstractWsClient {

    public PersonClient(ClientConfig clientConfig, ClientInterceptor[] interceptors) {
        super(clientConfig, interceptors);
    }

    public List<Person> getPersons() {
        GetPersonsResponse response = sendRequest(new GetPersonsRequest(), GetPersonsResponse.class);
        return response.getPersons();
    }

    public Person getPerson(Long id) {
        GetPersonRequest request = new GetPersonRequest();
        request.setId(id);
        GetPersonResponse response = sendRequest(request, GetPersonResponse.class);
        return response.getPerson();
    }

    public Person createPerson(Person person) {
        CreatePersonRequest request = new CreatePersonRequest();
        request.setPerson(person);
        CreatePersonResponse response = sendRequest(request, CreatePersonResponse.class);
        return response.getPerson();
    }

    public Person updatePerson(Long id, Person person) {
        person.setId(id);
        UpdatePersonRequest request = new UpdatePersonRequest();
        request.setPerson(person);
        UpdatePersonResponse response = sendRequest(request, UpdatePersonResponse.class);
        return response.getPerson();
    }

    public void deletePerson(Long id) {
        DeletePersonRequest request = new DeletePersonRequest();
        request.setId(id);
        sendRequest(request);
    }

}
