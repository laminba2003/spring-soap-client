package com.spring.training.client;

import com.spring.training.model.*;

import java.util.List;

public class CountryClient extends AbstractClient {

    public List<Country> getCountries() {
        GetCountriesResponse response = sendRequest(new GetCountriesRequest(), GetCountriesResponse.class);
        return response.getCountries();
    }

    public Country getCountry(String name) {
        GetCountryRequest request = new GetCountryRequest();
        request.setName(name);
        GetCountryResponse response = sendRequest(request, GetCountryResponse.class);
        return response.getCountry();
    }

    public Country createCountry(Country country) {
        CreateCountryRequest request = new CreateCountryRequest();
        request.setCountry(country);
        CreateCountryResponse response = sendRequest(request, CreateCountryResponse.class);
        return response.getCountry();
    }

    public Country updateCountry(String name, Country country) {
        country.setName(name);
        UpdateCountryRequest request = new UpdateCountryRequest();
        UpdateCountryResponse response = sendRequest(request, UpdateCountryResponse.class);
        return response.getCountry();
    }

    public void deleteCountry(String name) {
        DeleteCountryRequest request = new DeleteCountryRequest();
        Country country = new Country();
        country.setName(name);
        sendRequest(request, DeleteCountryResponse.class);
    }

}